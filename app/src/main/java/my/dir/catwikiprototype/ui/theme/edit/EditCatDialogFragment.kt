package my.dir.catwikiprototype.ui.theme.edit

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import my.dir.catwikiprototype.R
import my.dir.catwikiprototype.data.Cat
import my.dir.catwikiprototype.data.local.RoomCatRepository
import my.dir.catwikiprototype.data.CatRepository

class EditCatDialogFragment(
    private val cat: Cat,
    private val onCatUpdated: () -> Unit
) : DialogFragment() {

    private lateinit var repo: CatRepository
    private var imageUri: Uri? = null
    private lateinit var imgPreview: ImageView

    // Лончер для вибору зображення
    private val pickImageLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imgPreview.setImageURI(it)
        }
    }

    companion object {
        // Макет діалогу (той самий, що і для додавання)
        private val LAYOUT = R.layout.dialog_add_cat
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repo = RoomCatRepository(context)
        // Якщо раніше була картинка — зберігаємо URI
        imageUri = cat.imageUrl.takeIf { it.isNotBlank() }?.let(Uri::parse)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(LAYOUT, null)
        imgPreview = view.findViewById(R.id.imgPreview)
        val btnPick = view.findViewById<Button>(R.id.btnPickImage)
        val etName  = view.findViewById<EditText>(R.id.editCatName)
        val etDesc  = view.findViewById<EditText>(R.id.editCatDesc)

        // Встановлюємо початкові значення
        etName.setText(cat.name)
        etDesc.setText(cat.description)
        imageUri?.let { imgPreview.setImageURI(it) }

        btnPick.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Редагувати породу")
            .setView(view)
            .setPositiveButton("Зберегти") { _, _ ->
                val newName = etName.text.toString().trim()
                val newDesc = etDesc.text.toString().trim()
                if (newName.isNotEmpty() && newDesc.isNotEmpty()) {
                    val updated = cat.copy(
                        name = newName,
                        description = newDesc,
                        imageUrl = imageUri?.toString().orEmpty()
                    )
                    repo.updateCat(updated)
                    onCatUpdated()
                }
            }
            .setNegativeButton("Скасувати", null)
            .create()
    }
}