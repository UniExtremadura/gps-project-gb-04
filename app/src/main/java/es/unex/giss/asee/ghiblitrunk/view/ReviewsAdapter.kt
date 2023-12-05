package es.unex.giss.asee.ghiblitrunk.view
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.unex.giss.asee.ghiblitrunk.data.models.Review
import es.unex.giss.asee.ghiblitrunk.databinding.ItemReviewBinding
import es.unex.giss.asee.ghiblitrunk.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ReviewsAdapter(
    private var reviewsList: List<Review>,
    private val coroutineScope: CoroutineScope,
    private val context: Context?
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(
        private val binding: ItemReviewBinding,
        private val coroutineScope: CoroutineScope,
        private val context: Context?
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review, totalItems: Int) {
            coroutineScope.launch { // Lanzar una corrutina
                val username = context?.let { UserManager.loadCurrentUser(it)?.name }
                with(binding) {
                    tvUsername.text = username
                    tvReview.text = review.content
                }
            }
        }
    }

    override fun getItemCount() = reviewsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReviewBinding.inflate(inflater, parent, false)
        return ReviewViewHolder(binding, coroutineScope, context)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewsList[position]
        holder.bind(review, reviewsList.size)
    }

    fun updateData(updatedReviews: List<Review>) {
        reviewsList = updatedReviews
        notifyDataSetChanged()
    }
}
