package es.unex.giss.asee.ghiblitrunk.view.adapters
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giss.asee.ghiblitrunk.data.models.Comment
import es.unex.giss.asee.ghiblitrunk.databinding.ItemCommentBinding
import es.unex.giss.asee.ghiblitrunk.view.home.HomeViewModel

class CommentAdapter(
    private var commentsList: List<Comment>,
    private val homeViewModel: HomeViewModel,
    private val context: Context?
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(
        private val binding: ItemCommentBinding,
        private val homeViewModel: HomeViewModel,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            val username = context?.let { homeViewModel.user.value?.name}
            with(binding) {
                tvUsername.text = username.toString()
                tvComment.text = comment.text
                tvTimestamp.text = comment.timestamp.toString()
            }
        }
    }

    override fun getItemCount() = commentsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommentBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding, homeViewModel, context)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.bind(comment)
    }

    fun updateData(updatedComments: List<Comment>) {
        Log.d("COMMENT_ADAPTER", updatedComments.toString())
        commentsList = updatedComments
        notifyDataSetChanged()
    }
}
