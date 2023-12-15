package es.unex.giss.asee.ghiblitrunk.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import es.unex.giss.asee.ghiblitrunk.databinding.FragmentCommentsBinding
import es.unex.giss.asee.ghiblitrunk.view.adapters.CommentAdapter
import es.unex.giss.asee.ghiblitrunk.view.home.HomeViewModel

class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: CommentAdapter

    private val viewModel: CommentViewModel by viewModels { CommentViewModel.Factory }
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val args: CommentsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCommentsBinding.inflate(inflater, container, false)

        setUpListeners()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.toast.observe(viewLifecycleOwner) {text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        // Inicializa el RecyclerView
        setUpRecyclerView()

        // Suscribe el RecyclerView al adaptador
        subscribeUI()

        // Configura los listeners
        setUpListeners()
    }

    private fun subscribeUI() {
        val movie = args.movie

        // Llama al método del ViewModel para actualizar los comentarios
        viewModel.updateCommentsForMovie(movie.id)

        // Observa los cambios en los comentarios
        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            adapter.updateData(comments)
        }
    }

    private fun setUpListeners() {
        val movie = args.movie

        // Configura el botón para agregar un comentario
        with(binding) {
            btnSubmit.setOnClickListener {
                val comment = etComment.text.toString().trim()
                if (viewModel.isValidComment(comment)) {
                    // Añade el comentario a la película
                    val userId = homeViewModel.user.value?.userId
                    if (userId != null) {
                        viewModel.insertAndRelate(movie, userId)
                        viewModel.addCommentToMovie(movie.id, userId, comment)
                        etComment.text.clear() // Limpia el campo de entrada
                    } else {
                        Log.e("COMMENTS_FRAG", "El userId es nulo")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // avoid memory leaks
    }

    private fun setUpRecyclerView() {
        adapter = CommentAdapter(emptyList(), homeViewModel, requireContext())

        with(binding) {
            rvCommentsList.layoutManager = LinearLayoutManager(context)
            rvCommentsList.adapter = adapter
        }
    }
}