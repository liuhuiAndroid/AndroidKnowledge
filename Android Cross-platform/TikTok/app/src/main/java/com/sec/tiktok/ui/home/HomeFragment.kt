package com.sec.tiktok.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sec.tiktok.FlutterFragmentUtil
import com.sec.tiktok.R
import com.sec.tiktok.databinding.FragmentHomeBinding
import com.sec.tiktok.databinding.FragmentNotificationsBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val flutterFragment =
//            FlutterFragment.withNewEngine().initialRoute("/mine").build<FlutterFragment>()
//        parentFragmentManager.beginTransaction().replace(R.id.notification_fl, flutterFragment).commit()
        val createFlutterFragment =
            FlutterFragmentUtil.createFlutterFragment(requireActivity(), "camera", "/camera")
        parentFragmentManager.beginTransaction().replace(R.id.notification_fl, createFlutterFragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}