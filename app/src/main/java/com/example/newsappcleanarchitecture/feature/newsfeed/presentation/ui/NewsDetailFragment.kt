package com.example.newsappcleanarchitecture.feature.newsfeed.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsappcleanarchitecture.R
import com.example.newsappcleanarchitecture.databinding.FragmentNewsDetailBinding
import com.example.newsappcleanarchitecture.databinding.FragmentNewsFeedBinding
import com.example.newsappcleanarchitecture.feature.newsfeed.data.model.domainClass.News
import com.example.newsappcleanarchitecture.feature.newsfeed.presentation.viewmodel.NewsFeedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var news: News? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: NewsDetailFragmentArgs by navArgs()
        news = args.NewsArg
        setupView()
    }

    private fun setupView() {
        news?.let { news ->
            binding.apply {
                Glide.with(root.context).load(news.image).apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                ).into(detailImage)

                detailTitle.text = news.title
                detailDescription.text = news.description
                detailAuthor.text = news.author

            }
        }
        binding.composeView.setContent {
            news?.let { NewsDetail(it) }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

@Composable
fun NewsDetail(news: News) {
    // A surface container using the 'background' color from the theme
    Surface(color = MaterialTheme.colors.background) {
        Text(text = "Hello, ${news.title}")
    }
}
