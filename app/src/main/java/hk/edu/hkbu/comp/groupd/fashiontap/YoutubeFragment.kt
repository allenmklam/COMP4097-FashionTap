package hk.edu.hkbu.comp.groupd.fashiontap

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hk.edu.hkbu.comp.groupd.fashiontap.databinding.FragmentYoutubeBinding
import hk.edu.hkbu.comp.groupd.fashiontap.json.Item
import hk.edu.hkbu.comp.groupd.fashiontap.json.YoutubeChannelResponse
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.google.android.youtube.player.YouTubePlayerView
import retrofit2.Call
import retrofit2.Callback
import com.google.android.youtube.player.YouTubePlayerSupportFragment



class YoutubeFragment : Fragment() {

    lateinit var binding: FragmentYoutubeBinding

    companion object {
        val TAG: String = YoutubeFragment::class.java.simpleName
        fun newInstance() = YoutubeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_notifications)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_youtube, container, false)
        binding.contentChannel.listViewModel = ListViewModel<Item>(BR.channelItem, R.layout.content_channel_item)

        YoutubeService.instance.getYoutubeChannel().enqueue(object : Callback<YoutubeChannelResponse> {
            override fun onFailure(call: Call<YoutubeChannelResponse>, t: Throwable) {
                Log.e("ThreadActivity", t.message)
            }

            override fun onResponse(call: Call<YoutubeChannelResponse>, response: retrofit2.Response<YoutubeChannelResponse>) {
                if (response.isSuccessful) {
                    val channelItem = response.body()?.items as List<Item>
                    Log.d(
                            "getYoutubeChannel",
                            "channel name: " + channelItem?.get(0)?.snippet?.title
                                    +  "\nchannel description: " + channelItem?.get(0)?.snippet?.description
                                    +  "\nchannel thumbnails: " + channelItem?.get(0)?.snippet?.thumbnails?.high?.url
                                    +  "\nchannel viewCount: " + channelItem?.get(0)?.statistics?.viewCount
                                    +  "\nchannel subscriberCount: " + channelItem?.get(0)?.statistics?.subscriberCount
                                    +  "\nchannel videoCount: " + channelItem?.get(0)?.statistics?.videoCount
                    )
                    with(binding.contentChannel.listViewModel?.items as ObservableArrayList<Item>) {
                        clear()
                        addAll(channelItem)
                    }
                }
            }
        })

        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.player, youTubePlayerFragment).commit()

        youTubePlayerFragment.initialize("AIzaSyBfemQ9-xiY5eA0X6tKKFpaOa_ZXuLUZ5g",
                object : YouTubePlayer.OnInitializedListener {
                    override fun onInitializationSuccess(
                            provider: YouTubePlayer.Provider,
                            youTubePlayer: YouTubePlayer, b: Boolean
                    ) {

                        // do any work here to cue video, play video, etc.
//                    youTubePlayer.cueVideo("5xVh-7ywKpE")
                        youTubePlayer.loadPlaylist("UU3T5fmgL4Kvk3kG1kF6JFeA")
                    }

                    override fun onInitializationFailure(
                            provider: YouTubePlayer.Provider,
                            youTubeInitializationResult: YouTubeInitializationResult
                    ) {

                    }
                })

        return binding.root;
    }

}