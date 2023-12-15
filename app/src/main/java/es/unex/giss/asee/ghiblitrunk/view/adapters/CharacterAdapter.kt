package es.unex.giss.asee.ghiblitrunk.view.adapters
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import es.unex.giss.asee.ghiblitrunk.R
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.ItemCharacterBinding
import es.unex.giss.asee.ghiblitrunk.view.home.CharacterViewModel
import java.lang.reflect.Field
import java.util.Random

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
                // Mostramos la imagen del siguiente formato
                val imageName = "portrait_" + character?.name?.lowercase()?.replace(" ", "_")?.replace("'", "")
                // Obtener el ID de la imagen
                val resourceId = context?.resources?.getIdentifier(imageName, "drawable", context.packageName)
                Log.e("CHARACTER_ADAPTER", "El ID del recurso para $imageName es: $resourceId")

                if (resourceId != null && resourceId != 0) {
                    // Si encontramos el recurso lo añadimos al imageView
                    context?.let {
                        Glide.with(it)
                            .load(resourceId)
                            .into(ivImage)
                    }
                } else {
                    // Si no se encuentra, ocultamos el ImageView
                    binding.ivImage.visibility = View.GONE
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
