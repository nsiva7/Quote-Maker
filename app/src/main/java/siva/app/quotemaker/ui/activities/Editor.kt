package siva.app.quotemaker.ui.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import ja.burhanrashid52.photoeditor.*
import ja.burhanrashid52.photoeditor.PhotoEditor.OnSaveListener
import kotlinx.android.synthetic.main.editor.*
import siva.app.quotemaker.R
import siva.app.quotemaker.controls.adapter.EditingToolsAdapter
import siva.app.quotemaker.controls.adapter.FiltersAdapter
import siva.app.quotemaker.controls.enums.ToolType
import siva.app.quotemaker.controls.listeners.*
import siva.app.quotemaker.controls.util.Util
import siva.app.quotemaker.controls.util.Util.Companion.showSnackbar
import siva.app.quotemaker.ui.dialogs.EmojiBSDF
import siva.app.quotemaker.ui.dialogs.PropertiesBSDF
import siva.app.quotemaker.ui.dialogs.StickerBSDF
import siva.app.quotemaker.ui.dialogs.TextEditorD
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/*
* Created by Siva Nimmala on 11/8/20.
* */
class Editor : AppCompatActivity(), OnPhotoEditorListener {

    private val galleryRequest = 123
    private val cameraRequest = 125

    private lateinit var photoEditor: PhotoEditor
    private var cameraUri: Uri? = null
    private var imagePath: String? = null
    private var saveImageUri: Uri? = null
    private var isFilterVisible: Boolean = false
    private val constraintSet = ConstraintSet()
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.setFullScreen(this)
        setContentView(R.layout.editor)

        editor.source.setImageURI(intent.getParcelableExtra("image"))

        initTools()

        val tfRobotoText = ResourcesCompat.getFont(this, R.font.roboto_medium)

        val tvEmoji = Typeface.createFromAsset(assets, "emojione-android.ttf")

        photoEditor = PhotoEditor.Builder(applicationContext, editor)
            .setPinchTextScalable(true)
            .setDefaultTextTypeface(tfRobotoText)
            .setDefaultEmojiTypeface(tvEmoji)
            .build()

        photoEditor.setOnPhotoEditorListener(this)
    }

    private fun initTools() {
        ivBrush.setOnClickListener { photoEditor.setBrushDrawingMode(true)
            tvTitle.setText(R.string.label_brush)
            PropertiesBSDF(object : PropertyListener {
                override fun onColorChanged(color: Int) {
                    photoEditor.brushColor = color
                    tvTitle.setText(R.string.label_brush)
                }

                override fun onOpacityChanged(opacity: Int) {
                    photoEditor.setOpacity(opacity)
                    tvTitle.setText(R.string.label_brush)
                }

                override fun onBrushSizeChanged(brushSize: Int) {
                    photoEditor.brushSize = brushSize.toFloat()
                    tvTitle.setText(R.string.label_brush)
                }
            }).show(supportFragmentManager, "") }
        ivText.setOnClickListener { TextEditorD(object : TextEditorListener {
            override fun onTextDone(input: String, color: Int) {
                val textStyle = TextStyleBuilder()
                textStyle.withTextColor(color)
                photoEditor.addText(input, textStyle)
                tvTitle.setText(R.string.label_text)
            }
        }).show(supportFragmentManager, "") }
        ivEraser.setOnClickListener { photoEditor.brushEraser()
            tvTitle.setText(R.string.label_eraser_mode) }
        ivFilter.setOnClickListener { tvTitle.setText(R.string.label_filter)
            showFilter(true) }
        ivEmoji.setOnClickListener { EmojiBSDF(object : EmojiListener {
            override fun onEmojiSelected(emoji: String) {
                photoEditor.addEmoji(emoji)
                tvTitle.setText(R.string.label_emoji)
            }
        }).show(supportFragmentManager, "") }
        ivSticker.setOnClickListener { StickerBSDF(object : StickerListener {
            override fun onStickerSelected(bitmap: Bitmap) {
                photoEditor.addImage(bitmap)
                tvTitle.setText(R.string.label_sticker)
            }
        }).show(supportFragmentManager, "") }

        tvBrush.setOnClickListener { ivBrush.performClick() }
        tvText.setOnClickListener { ivText.performClick() }
        tvEraser.setOnClickListener { ivEraser.performClick() }
        tvFilter.setOnClickListener { ivFilter.performClick() }
        tvEmoji.setOnClickListener { ivEmoji.performClick() }
        tvSticker.setOnClickListener { ivSticker.performClick() }

        rvFilters.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)
        rvFilters.adapter = FiltersAdapter(object : FilterListener {
            override fun onSelectFilter(photoFilter: PhotoFilter) {
                photoEditor.setFilterEffect(photoFilter)
            }
        })

        ivUndo.setOnClickListener { photoEditor.undo() }
        ivRedo.setOnClickListener { photoEditor.redo() }
        ivBack.setOnClickListener { onBackPressed() }
        ivSave.setOnClickListener { saveImage() }
        ivShare.setOnClickListener { shareImage() }
        ivClear.setOnClickListener { photoEditor.clearAllViews() }
        ivGallery.setOnClickListener {
            startActivityForResult(
                Intent.createChooser(Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT), "Select Image"),
                galleryRequest
            )
        }
        ivCamera.setOnClickListener {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val file = File(Environment.getExternalStorageDirectory(), "/Quote_Maker/capture/Quote_Maker_Photo_$timeStamp.png")

            if (!file.parentFile!!.exists()) {
                file.parentFile!!.mkdirs()
            }

            cameraUri = FileProvider.getUriForFile(
                this,
                "siva.app.quotemaker.fileprovider",
                file
            )

            imagePath = file.absolutePath

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            startActivityForResult(intent, cameraRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            galleryRequest -> {
                photoEditor.clearAllViews()
                editor.source.setImageURI(data!!.data)
            }
            cameraRequest -> {
                photoEditor.clearAllViews()

                val image: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, cameraUri)
                editor.source.setImageBitmap(Util.getRotateImage(imagePath, image))
            }
        }
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        TextEditorD(object : TextEditorListener {
            override fun onTextDone(input: String, color: Int) {
                val textStyle = TextStyleBuilder()
                textStyle.withTextColor(color)
                photoEditor.addText(input, textStyle)
                tvTitle.setText(R.string.label_text)
            }
        }, colorCode, text!!).show(supportFragmentManager, "")
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {

    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {

    }

    override fun onStopViewChangeListener(viewType: ViewType?) {

    }

    fun showFilter(isVisible: Boolean) {
        isFilterVisible = isVisible
        constraintSet.clone(clRoot)
        if (isVisible) {
            constraintSet.clear(rvFilters.id, ConstraintSet.START)
            constraintSet.connect(
                rvFilters.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            constraintSet.connect(
                rvFilters.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
        } else {
            constraintSet.connect(
                rvFilters.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            constraintSet.clear(rvFilters.id, ConstraintSet.END)
        }
        val changeBounds = ChangeBounds()
        changeBounds.duration = 350
        changeBounds.interpolator = AnticipateOvershootInterpolator(1.0f)
        TransitionManager.beginDelayedTransition(clRoot, changeBounds)
        constraintSet.applyTo(clRoot)
    }

    override fun onBackPressed() {
        if (isFilterVisible) {
            showFilter(false)
            tvTitle.setText(R.string.app_name)
        } else if (!photoEditor.isCacheEmpty) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showSaveDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.msg_save_image))
        builder.setPositiveButton("Save") { _, _ -> saveImage() }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.setNeutralButton("Discard") { _, _ -> finish() }
        builder.create().show()
    }

    private fun saveImage() {
        showLoading()

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(Environment.getExternalStorageDirectory(), "/Quote_Maker/Quotes/QuoteMaker_Quote_$timeStamp.png")

        if (!file.parentFile!!.exists()) {
            file.parentFile!!.mkdirs()
        }

        try {
            file.createNewFile()
            val saveSettings = SaveSettings.Builder()
                .setClearViewsEnabled(true)
                .setTransparencyEnabled(true)
                .build()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                showSnackbar(this, getString(R.string.noStoragePermissions))
                return
            }
            photoEditor.saveAsFile(file.absolutePath, saveSettings, object : OnSaveListener {
                override fun onSuccess(imagePath: String) {
                    hideLoading()
                    showSnackbar(this@Editor, getString(R.string.imageSaved))
                    saveImageUri = Uri.fromFile(File(imagePath))
                    editor.source.setImageURI(saveImageUri)
                }

                override fun onFailure(exception: Exception) {
                    hideLoading()
                    showSnackbar(this@Editor, getString(R.string.imageSaveFailed))
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
            hideLoading()
            showSnackbar(this, e.message!!)
        }
    }

    fun hideLoading() {
        progressDialog.dismiss()
    }

    private fun showLoading() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Saving...")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun shareImage() {
        if (saveImageUri == null) {
            showLoading()

            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val file = File(Environment.getExternalStorageDirectory(), "/Quote_Maker/Quotes/QuoteMaker_Quote_$timeStamp.png")

            if (!file.parentFile!!.exists()) {
                file.parentFile!!.mkdirs()
            }

            try {
                file.createNewFile()
                val saveSettings = SaveSettings.Builder()
                    .setClearViewsEnabled(true)
                    .setTransparencyEnabled(true)
                    .build()
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    showSnackbar(this, getString(R.string.noStoragePermissions))
                    return
                }
                photoEditor.saveAsFile(file.absolutePath, saveSettings, object : OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        hideLoading()
                        showSnackbar(this@Editor, getString(R.string.imageSaved))
                        saveImageUri = Uri.fromFile(File(imagePath))
                        editor.source.setImageURI(saveImageUri)

                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_STREAM, Util.buildFileProviderUri(this@Editor, saveImageUri!!))
                        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
                    }

                    override fun onFailure(exception: Exception) {
                        hideLoading()
                        showSnackbar(this@Editor, getString(R.string.imageSaveFailed))
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
                hideLoading()
                showSnackbar(this, e.message!!)
            }

            return
        }
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, Util.buildFileProviderUri(this, saveImageUri!!))
        startActivity(Intent.createChooser(intent, getString(R.string.msg_share_image)))
    }
}