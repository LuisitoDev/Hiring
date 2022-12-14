package com.psm.hiring.ui.jobs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psm.hiring.R
import com.psm.hiring.adapters.jobsViewPagerAdapter
import com.psm.hiring.databinding.FragmentJobsBinding
import com.psm.hiring.ui.account.AccountFragment
import com.psm.hiring.ui.home.HomeFragment
import com.psm.hiring.ui.myapplications.MyApplicationsFragment
import com.psm.hiring.ui.myjobs.MyJobsFragment

class JobsFragment : Fragment() {

    private lateinit var jobsViewModel: JobsViewModel
    private var _binding: FragmentJobsBinding? = null
    private lateinit var jobsViewPagerAdapter : jobsViewPagerAdapter
    private lateinit var viewPagerJobs: ViewPager2


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        jobsViewModel =
            ViewModelProvider(this).get(JobsViewModel::class.java)

        _binding = FragmentJobsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPagerJobs = binding.viewPagerJobs

        jobsViewPagerAdapter = jobsViewPagerAdapter(childFragmentManager, lifecycle)
        jobsViewPagerAdapter.addFragment(MyJobsFragment())
        jobsViewPagerAdapter.addFragment(MyApplicationsFragment())

        viewPagerJobs.adapter = jobsViewPagerAdapter

        val tabLayout : TabLayout = binding.tabLayout
        val stringTabArray : List<String> = listOf(getString(R.string.ti_myJobs), getString(R.string.ti_myApps))

        TabLayoutMediator(tabLayout, viewPagerJobs){
            tab, position ->
            tab.text = stringTabArray[position]
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}