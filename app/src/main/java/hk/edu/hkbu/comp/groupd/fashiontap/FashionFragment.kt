package hk.edu.hkbu.comp.groupd.fashiontap

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hk.edu.hkbu.comp.groupd.fashiontap.json.Photo
import hk.edu.hkbu.comp.groupd.fashiontap.databinding.FragmentFashionBinding
import hk.edu.hkbu.comp.groupd.fashiontap.json.PhotoResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_fashion.*
import retrofit2.Call
import retrofit2.Callback
import kotlin.concurrent.thread
import android.support.v4.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.content_fashion.*


class FashionFragment : Fragment() {
    lateinit var binding: FragmentFashionBinding
    var privateThreads: List<Photo> = emptyList()
    var isShowing: Boolean = false

    companion object {
        val TAG: String = FashionFragment::class.java.simpleName
        fun newInstance() = FashionFragment()
//        private var fashionFragment = FashionFragment()
//        fun newInstance() = fashionFragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_dashboard)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fashion, container, false)
        binding.contentFashion.listViewModel = ListViewModel<Photo>(BR.threadItem, R.layout.content_fashion_item)

        initThreads()

        return binding.root;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view:View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.contentFashion.swipeContainer.setOnRefreshListener {
            refreshThreads()
        }

        if(privateThreads.isEmpty())
            showProgress(true)
    }

    override fun onDetach() {
        super.onDetach()

        showProgress(false)
    }

    fun initThreads() {
        if(privateThreads.isEmpty()) {
            FashionService.instance.getFashionPhoto().enqueue(object : Callback<PhotoResponse> {
                override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                    Log.e("FashionFragment", t.message)
                }

                override fun onResponse(call: Call<PhotoResponse>, response: retrofit2.Response<PhotoResponse>) {
                    if (response.isSuccessful) {

                        showProgress(false)

                        val threads = response.body()?.photo_stream?.photos as List<Photo>
                        privateThreads = threads
                        for (thread in threads) {
                            Log.d(
                                    "getFashionPhoto",
                                    thread.user.username + " " + thread.user.name + " before " + System.currentTimeMillis()
                            )

                        }
//                    Log.d("test123",threads.toString())
                        with(binding.contentFashion.listViewModel?.items as ObservableArrayList<Photo>) {
                            clear()
                            addAll(threads)
                        }
                    }
                }
            })
        } else {
            with(binding.contentFashion.listViewModel?.items as ObservableArrayList<Photo>) {
                clear()
                addAll(privateThreads)
            }
        }
    }

    fun refreshThreads() {
        FashionService.instance.getFashionPhoto().enqueue(object : Callback<PhotoResponse> {
            override fun onFailure(call: Call<PhotoResponse>, t: Throwable) {
                Log.e("FashionFragment", t.message)
            }

            override fun onResponse(call: Call<PhotoResponse>, response: retrofit2.Response<PhotoResponse>) {
                if (response.isSuccessful) {

                    if(binding.contentFashion.swipeContainer.isRefreshing)
                        binding.contentFashion.swipeContainer.isRefreshing = false

                    val threads = response.body()?.photo_stream?.photos as List<Photo>
                    for (thread in threads) {
                        Log.d(
                                "getFashionPhoto",
                                thread.user.username + " " + thread.user.name + " before " + System.currentTimeMillis()
                        )

                    }
//                    Log.d("test123",threads.toString())
                    with(binding.contentFashion.listViewModel?.items as ObservableArrayList<Photo>) {
                        clear()
                        addAll(threads)
                    }
                }
            }
        })
    }

    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            binding.fashionProgress.visibility = if (show) View.VISIBLE else View.GONE
            binding.fashionProgress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            binding.fashionProgress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            binding.fashionProgress.visibility = if (show) View.VISIBLE else View.GONE
            binding.fashionProgress.visibility = if (show) View.GONE else View.VISIBLE
        }
    }



}