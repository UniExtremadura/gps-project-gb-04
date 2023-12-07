package es.unex.giss.asee.ghiblitrunk.view.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.ItemCharacterBinding
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel

class CharacterAdapter(
    private var charactersList: List<Character>,
    private val onClickItem: (Character) -> Unit,
    private val viewModel: CharacterViewModel,
    private val context: Context?
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    // Obtener el ID de la imagen por su nombre
    private fun getImageResourceId(imageName: String): Int {
        return context?.resources?.getIdentifier(
            imageName,
            "drawable",
            "${context.packageName}.character_wallpapers"
        ) ?: 0
    }

    // Lista de imágenes que cargará el adapter
    private val wallpapersList = mutableListOf<Int>()

    init {
        // Cargar las imágenes en la lista usando el patrón de nombres
        val resources = context?.resources
        if (resources != null) {
            for (i in 1..10) {
                val imageName = "img_char_$i"
                val imageId = getImageResourceId(imageName)
                if (imageId != 0) {
                    wallpapersList.add(imageId)
                }
            }
        }
    }

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val onClickItem: (Character) -> Unit,
        private val viewModel: CharacterViewModel,
        private val context: Context?,
        private val wallpapersList: List<Int>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character, position: Int) {
            with(binding){// Asignamos las características del item
                if(position != -1){
                    // Asignar la imagen al ImageView
                    ivImage.setImageResource(wallpapersList[position])
                }

                tvName.text = character.name
                tvGender.text = character.gender

                // Configurar onClick
                ivLike.setOnClickListener {
                    viewModel.onClickLike(character)
                }

                // Configurar el clic al pulsar en el resto de items del card_view
                root.setOnClickListener{
                    onClickItem(character)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacterBinding.inflate(inflater, parent, false)
        return CharacterViewHolder(binding, onClickItem, viewModel, context, wallpapersList)
    }

    override fun getItemCount() = charactersList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = charactersList[position]

        // Verificar si wallpapersList tiene elementos antes de realizar cálculos
        if (wallpapersList.isNotEmpty()) {
            val imagePosition = position % wallpapersList.size
            holder.bind(character, imagePosition)
        } else {
            // Manejar el caso donde wallpapersList está vacía
            // Por ejemplo, puedes asignar una imagen predeterminada
            holder.bind(character, -1) // O asignar el índice que desees para la imagen
        }
    }

    fun updateData(newList: List<Character>) {
        // Actualizamos la lista que usa el adaptador
        charactersList = newList
        notifyDataSetChanged()
    }
}
