package es.unex.giss.asee.ghiblitrunk.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import es.unex.giss.asee.ghiblitrunk.database.GhibliTrunkDatabase
import es.unex.giss.asee.ghiblitrunk.data.models.Comment
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCommentsBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CommentAdapter
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CommentsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: GhibliTrunkDatabase
    private var _binding: FragmentCommentsBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CommentAdapter
    private lateinit var reviewsList: List<Comment>
    private val args: CommentsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCommentsBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        setUpListeners()
        return binding.root

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = GhibliTrunkDatabase.getInstance(context)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpListeners()
    }

    private fun setUpListeners() {
        val movie = args.movie
        with(binding) {
            btnSubmit.setOnClickListener {
                val reviewText = etReview.text.toString().trim()

                if (isValidReview(reviewText)) {
                    /*
                        lifecycleScope.launch {
                            val userId = UserManager.loadCurrentUser(requireContext())?.userId
                            Log.e("REVIEWS_FRAGMENT", "$userId")
                            Log.e("REVIEWS_FRAGMENT", "${movie.id}")
                            // Insertar la reseña en la base de datos
                            userId?.let { userId ->
                                movie.id?.let { movieId ->
                                    Review(null,
                                        movieId, userId, reviewText)
                                }
                            }?.let { it2 -> db.reviewDao().insertReview(it2) }
                            // Actualizar la lista de reseñas en el RecyclerView
                            setUpRecyclerView()
                        }*/

                    // Limpiar el campo de entrada después de enviar la reseña
                    etReview.text.clear()
                } else {
                    // La reseña no es válida, mostrar un Toast con el mensaje de error
                    Toast.makeText(context, "La reseña debe tener entre 1 y 100 caracteres.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    private fun setUpRecyclerView() {
        Log.e("COMMENTS_FRAG", "Setting up the RecyclerView")
        lifecycleScope.launch {
            /*
            val news = args.news
            val userId = UserManager.loadCurrentUser(requireContext())?.userId ?: 0L

            if (!::adapter.isInitialized) {
                reviewsList = db.reviewDao().getUserReviews(userId, news.newsId ?: 0L)
                adapter = ReviewsAdapter(reviewsList, lifecycleScope, requireContext())

                binding.rvReviewsList.layoutManager = LinearLayoutManager(context)
                binding.rvReviewsList.adapter = adapter
            } else {
                val updatedReviews = db.reviewDao().getUserReviews(userId, news.newsId ?: 0L)
                adapter.updateData(updatedReviews)
            }

             */
        }
    }

    private fun isValidReview(reviewText: String): Boolean {
        // Verificar que la reseña tenga al menos 1 carácter y menos de 100 caracteres
        return reviewText.length in 1..100
    }
}