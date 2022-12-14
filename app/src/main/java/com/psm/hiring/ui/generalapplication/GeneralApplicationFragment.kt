package com.psm.hiring.ui.generalapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psm.hiring.R
import com.psm.hiring.adapters.generalApplicationViewPagerAdapter
import com.psm.hiring.adapters.jobsViewPagerAdapter
import com.psm.hiring.databinding.FragmentGeneralApplicationsBinding
import com.psm.hiring.ui.applications.ApplicationsFragment
import com.psm.hiring.ui.hired.HiredFragment
import com.psm.hiring.ui.myapplications.MyApplicationsFragment
import com.psm.hiring.ui.myjobs.MyJobsFragment

class GeneralApplicationFragment : Fragment() {

    private lateinit var generalApplicationViewModel: GeneralApplicationViewModel
    private var _binding: FragmentGeneralApplicationsBinding? = null
    private lateinit var generalApplicationViewPagerAdapter : generalApplicationViewPagerAdapter
    private lateinit var viewPagerApplications: ViewPager2

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        generalApplicationViewModel =
            ViewModelProvider(this).get(GeneralApplicationViewModel::class.java)

        _binding = FragmentGeneralApplicationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bundle = arguments
        var IdTrabajo : String = ""
        if (bundle?.isEmpty == false) {
            IdTrabajo =
                bundle!!.getString(ApplicationsFragment.ARG_ID_SOLICITUD_VISUALIZE_JOB) ?: ""
        } else {
            var error = "hubo un error, no se mando un id"
            //TODO: DESPLEGAR UN ERROR Y SALIR DE LA VENTANA
        }

        viewPagerApplications = binding.viewPagerApplications

        generalApplicationViewPagerAdapter = generalApplicationViewPagerAdapter(childFragmentManager, lifecycle)
        generalApplicationViewPagerAdapter.addFragment(ApplicationsFragment(IdTrabajo))
        generalApplicationViewPagerAdapter.addFragment(HiredFragment(IdTrabajo))

        viewPagerApplications.adapter = generalApplicationViewPagerAdapter

        val tabLayout : TabLayout = binding.tabLayout
        val stringTabArray : List<String> = listOf(getString(R.string.tab_pending), getString(R.string.tab_hired))

        TabLayoutMediator(tabLayout, viewPagerApplications){
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