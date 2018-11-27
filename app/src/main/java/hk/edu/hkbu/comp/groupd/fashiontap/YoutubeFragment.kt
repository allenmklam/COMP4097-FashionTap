package hk.edu.hkbu.comp.groupd.fashiontap

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class YoutubeFragment : Fragment() {

    companion object {
        val TAG: String = YoutubeFragment::class.java.simpleName
        fun newInstance() = YoutubeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.title = getString(R.string.title_notifications)
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        return view
    }

}