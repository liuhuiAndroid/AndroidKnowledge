package com.sec.tiktok.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sec.tiktok.FlutterFragmentUtil
import com.sec.tiktok.R
import com.sec.tiktok.databinding.FragmentNotificationsBinding
import io.flutter.embedding.android.FlutterFragment

// https://zhuanlan.zhihu.com/p/375490787
class NotificationsFragment : Fragment() {

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
            FlutterFragmentUtil.createFlutterFragment(requireActivity(), "mine", "/mine")
        parentFragmentManager.beginTransaction().replace(R.id.notification_fl, createFlutterFragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}