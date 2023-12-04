package es.unex.giss.asee.ghiblitrunk.view.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giss.asee.ghiblitrunk.data.models.Character
import es.unex.giss.asee.ghiblitrunk.databinding.ItemCharacterBinding
import es.unex.giss.asee.ghiblitrunk.view.cardnews.CardCharacterManager

class CharacterAdapter(
    private var charactersList: List<Character>,
    private val onClickItem: (Character) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(
        private val binding: ItemCharacterBinding,
        private val onClickItem: (Character) -> Unit,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        private val cardManager = context?.let { CardCharacterManager(it) }
        fun bind(character: Character, totalItems: Int) {
            with(binding){
                // Asignar valores a las vistas
                tvTitle.text = character.name
                tvDescription.text = character.films.toString()

                // Configurar el clic en el botón de "like"
                ivLike.setOnClickListener {
                    cardManager?.onClickLike(character)
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
        return CharacterViewHolder(binding, onClickItem, context)
    }

    override fun getItemCount() = charactersList.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val news = charactersList[position]
        holder.bind(news, charactersList.size)
    }

    fun updateData(favNews: List<Character>) {
        // Actualizamos la lista que usa el adaptador
        charactersList = favNews
        notifyDataSetChanged()
    }
}
