package my.dir.catwikiprototype.ui.theme.add

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
import my.dir.catwikiprototype.utils.SortOption

class AddCatDialogFragment(
    private val onCatAdded: () -> Unit
) : DialogFragment() {

    private lateinit var repo: RoomCatRepository
    private var imageUri: Uri? = null
    private lateinit var imgPreview: ImageView

    // Лончер для вибору зображення
    private val pickImageLauncher = registerForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imgPreview.setImageURI(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        repo = RoomCatRepository(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(R.layout.dialog_add_cat, null)
        imgPreview = view.findViewById(R.id.imgPreview)
        val btnPick = view.findViewById<Button>(R.id.btnPickImage)
        val etName  = view.findViewById<EditText>(R.id.editCatName)
        val etDesc  = view.findViewById<EditText>(R.id.editCatDesc)

        btnPick.setOnClickListener {
            // Запускаємо стандартний провайдер контенту
            pickImageLauncher.launch("image/*")
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Додати нову породу")
            .setView(view)
            .setPositiveButton("Додати") { _, _ ->
                val name = etName.text.toString().trim()
                val desc = etDesc.text.toString().trim()
                if (name.isEmpty() || desc.isEmpty()) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Будь ласка, заповніть усі поля")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    val current = repo.getCats(SortOption.ALPHABETICAL)
                    val newId = (current.maxOfOrNull { it.id } ?: 0) + 1
                    val uriStr = imageUri?.toString() ?: ""
                    repo.addCat(Cat(newId, name, desc, uriStr, null, false))
                    onCatAdded()
                }
            }
            .setNegativeButton("Скасувати", null)
            .create()
    }
}