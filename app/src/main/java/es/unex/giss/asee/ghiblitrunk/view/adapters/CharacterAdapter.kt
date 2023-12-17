package es.unex.giss.asee.ghiblitrunk.view.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
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

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val onClickItem: (Character) -> Unit,
        private val viewModel: CharacterViewModel,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character) {
            with(binding){// Asignamos las características del item
                // Obtenemos el ID de la imagen a mostrar
                val imageName = "portrait_" + character?.name?.lowercase()?.replace(" ", "_")?.replace("'", "")
                val imageId = context?.resources?.getIdentifier(imageName, "drawable", context.packageName)

                // Se muestra si se encuentra la imagen asociada al personaje
                if (imageId != null && imageId != 0) {
                    ivImage.setImageResource(imageId)
                } else {
                    // Si no se encuentra, ocultamos el ImageView
                    binding.ivImage.visibility = View.GONE
                }

                tvName.text = character.name
                tvGender.text = character.gender

                // Configurar el like
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
        return CharacterViewHolder(binding, onClickItem, viewModel, context)
    }

    override fun getItemCount() = charactersList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = charactersList[position]

        holder.bind(character) // O asignar el índice que desees para la imagen
    }

    fun updateData(newList: List<Character>) {
        // Actualizamos la lista que usa el adaptador
        charactersList = newList
        notifyDataSetChanged()
    }
}
