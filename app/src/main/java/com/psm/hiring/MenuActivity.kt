package com.psm.hiring

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.psm.hiring.General.*
import com.psm.hiring.Modelos.Usuarios_Model
import com.psm.hiring.Utils.DataManager
import com.psm.hiring.databinding.ActivityMenuBinding
import com.psm.hiring.ui.applications.ApplicationsFragment
import com.psm.hiring.ui.offerjob.OfferJobFragment
import com.psm.hiring.ui.profile.Edit1of2Fragment
import com.psm.hiring.ui.profile.Edit2of2Fragment
import com.psm.hiring.ui.visualizejob.visualizeJobFragment
import java.math.BigInteger
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import com.leonardosantos.consumirwebapi.ImageUtilities
import com.psm.hiring.Database.UserApplication
import com.psm.hiring.Utils.NetworkStateReceiver


class MenuActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener,
    NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
    OnFragmentActionsListener_Account, OnRecyclerViewActionsListener_Home, OnRecyclerViewActionsListener_AdvanceSearch,
    OnRecyclerViewActionsListener_MyJobs, OnRecyclerViewActionsListener_MyApplications,
    OnRecyclerViewActionsListener_Notifications, OnFragmentActionsListener_Edit, OnFragmentActionsListener_OfferJob,
    NetworkStateReceiver.NetworkStateReceiverListener, OnFragmentActionsListener_Job {

    private lateinit var binding: ActivityMenuBinding

    var tvMenu: TextView? = null

    private lateinit var networkStateReceiver: NetworkStateReceiver


    override fun onDestroy() {
        super.onDestroy()
        networkStateReceiver.removeListener(this);
        this.unregisterReceiver(networkStateReceiver);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataManager.context = this

        networkStateReceiver = NetworkStateReceiver()
        networkStateReceiver.addListener(this)
        this.registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )


        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val drawerNavView : NavigationView = binding.navigationView
        val btnOffcanvas : View = binding.btnOffcanvas

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_menu) as NavHostFragment
        val navController = navHostFragment.navController

       navView.setupWithNavController(navController)
       drawerNavView.setupWithNavController(navController)

       navView.setOnItemSelectedListener(this)
       drawerNavView.setNavigationItemSelectedListener(this)
       btnOffcanvas.setOnClickListener(this)



        obtenerImagenAvatarUsuario()
    }

    private fun obtenerImagenAvatarUsuario(){
        if (DataManager.isOnline()) {
            DataManager.getUsuarioActivo({ usuarioResponse ->
                if (usuarioResponse != null)
                    editarAvatar(usuarioResponse);
            })
        }
        else{
            var IdUsuario = DataManager.IdUsuario
            if (IdUsuario != null) {
                val usuarioResponse = UserApplication.dbHelper.daoUsuarios.getUsuario(IdUsuario)

                if (usuarioResponse != null)
                    editarAvatar(usuarioResponse);
            }
        }
    }

    private fun editarAvatar(usuarioResponse: Usuarios_Model) {
        var navigationView = binding.navigationView.getHeaderView(0)
        var avatar_name = navigationView.findViewById<TextView>(R.id.nav_avatar_name)
        avatar_name.text = usuarioResponse.getNombreCompleto()

        var strImage =  usuarioResponse.ImagenPerfilUsuario!!.replace("data:image/png;base64,","").replace("data:image/jpeg;base64,","")
        var byteArray = Base64.decode(strImage, Base64.DEFAULT)


        var avatar_image = navigationView.findViewById<ImageView>(R.id.nav_avatar_image)
        avatar_image.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_menu) as NavHostFragment
        val navController = navHostFragment.navController
        tvMenu = binding.tvMenu

        var isLoggintOut = false

        when(item.itemId){
            R.id.navigation_account -> {
                this.tvMenu!!.text = getString(R.string.title_account)

            }
            R.id.navigation_home -> {
                this.tvMenu!!.text = getString(R.string.title_home)
            }
            R.id.navigation_messages -> {
                this.tvMenu!!.text = getString(R.string.title_messages)
            }
            R.id.navigation_notifications-> {
                this.tvMenu!!.text = getString(R.string.title_notifications)
            }
            R.id.navigation_jobs -> {
                this.tvMenu!!.text = getText(R.string.title_jobs)
            }
            R.id.navigation_offerJob -> {
                this.tvMenu!!.text = getText(R.string.title_offerJob)
            }
            R.id.navigation_applications -> {
                this.tvMenu!!.text = getString(R.string.title_applications)
            }
            R.id.navigation_logOut -> {
                isLoggintOut = true
            }
        }

        if (!isLoggintOut)
            NavigationUI.onNavDestinationSelected(item, navController)
        else
            cerrarSesion()

        return true
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_offcanvas -> {
                val navDrawer = binding.drawerLayout
                if (navDrawer.isDrawerOpen(GravityCompat.START))
                    navDrawer.closeDrawer(GravityCompat.START)
                else
                    navDrawer.openDrawer(GravityCompat.START)
            }

        }
    }


    override fun OnClickFragmentButton_Account_EditarPerfil(p_Usuario: Usuarios_Model?) {
        this.tvMenu!!.text = getText(R.string.title_edite_profile)

        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            DataManager.ImageAux = ""
            DataManager.ImageAux = p_Usuario?.ImagenPerfilUsuario
            //putString(Edit1of2Fragment.ARG_IMAGEN_USUARIO_EDIT_1, p_Usuario?.ImagenPerfilUsuario)
            putString(Edit1of2Fragment.ARG_NOMBRE_USUARIO_EDIT_1, p_Usuario?.NombreUsuario)
            putString(Edit1of2Fragment.ARG_AP_PATERNO_USUARIO_EDIT_1, p_Usuario?.ApellidoPaternoUsuario)
            putString(Edit1of2Fragment.ARG_AP_MATERNO_USUARIO_EDIT_1, p_Usuario?.ApellidoMaternoUsuario)

            putString(Edit1of2Fragment.ARG_FECHA_NAC_USUARIO_EDIT_1, p_Usuario?.FechaNacimientoUsuario)
            putString(Edit1of2Fragment.ARG_ESCOLARIDAD_USUARIO_EDIT_1, p_Usuario?.EscolaridadUsuario)
            putString(Edit1of2Fragment.ARG_PROFESION_USUARIO_EDIT_1, p_Usuario?.ProfesionUsuario)
            putString(Edit1of2Fragment.ARG_DESCRIPCION_USUARIO_EDIT_1, p_Usuario?.DescripcionUsuario)
        }

        navhost.findNavController().navigate(R.id.navigation_edit_profile_1of2, bundle)
    }

    override fun OnClickFragmentButton_Account_CambiarPassword() {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        navhost.findNavController().navigate(R.id.navigation_change_password)
    }

    override fun OnClickFragmentButton_Account_EditarPassword() {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        navhost.findNavController().navigate(R.id.navigation_account)
    }


    override fun OnClickFragmentButton_Edit2_GoToEdit1(p_Usuario: Usuarios_Model) {

        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            DataManager.ImageAux = ""
            DataManager.ImageAux = p_Usuario?.ImagenPerfilUsuario
            //putString(Edit1of2Fragment.ARG_IMAGEN_USUARIO_EDIT_1, p_Usuario?.ImagenPerfilUsuario)
            putString(Edit1of2Fragment.ARG_NOMBRE_USUARIO_EDIT_1, p_Usuario?.NombreUsuario)
            putString(Edit1of2Fragment.ARG_AP_PATERNO_USUARIO_EDIT_1, p_Usuario?.ApellidoPaternoUsuario)
            putString(Edit1of2Fragment.ARG_AP_MATERNO_USUARIO_EDIT_1, p_Usuario?.ApellidoMaternoUsuario)

            putString(Edit1of2Fragment.ARG_FECHA_NAC_USUARIO_EDIT_1, p_Usuario?.FechaNacimientoUsuario)
            putString(Edit1of2Fragment.ARG_ESCOLARIDAD_USUARIO_EDIT_1, p_Usuario?.EscolaridadUsuario)
            putString(Edit1of2Fragment.ARG_PROFESION_USUARIO_EDIT_1, p_Usuario?.ProfesionUsuario)
            putString(Edit1of2Fragment.ARG_DESCRIPCION_USUARIO_EDIT_1, p_Usuario?.DescripcionUsuario)
        }

        navhost.findNavController().navigate(R.id.navigation_edit_profile_1of2, bundle)
    }

    override fun OnClickFragmentButton_Edit1_GoToEdit2(p_Usuario: Usuarios_Model) {

        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            DataManager.ImageAux = ""
            DataManager.ImageAux = p_Usuario?.ImagenPerfilUsuario
            //putString(Edit2of2Fragment.ARG_IMAGEN_USUARIO_EDIT_2, p_Usuario?.ImagenPerfilUsuario)
            putString(Edit2of2Fragment.ARG_NOMBRE_USUARIO_EDIT_2, p_Usuario?.NombreUsuario)
            putString(Edit2of2Fragment.ARG_AP_PATERNO_USUARIO_EDIT_2, p_Usuario?.ApellidoPaternoUsuario)
            putString(Edit2of2Fragment.ARG_AP_MATERNO_USUARIO_EDIT_2, p_Usuario?.ApellidoMaternoUsuario)

            putString(Edit2of2Fragment.ARG_FECHA_NAC_USUARIO_EDIT_2, p_Usuario?.FechaNacimientoUsuario)
            putString(Edit2of2Fragment.ARG_ESCOLARIDAD_USUARIO_EDIT_2, p_Usuario?.EscolaridadUsuario)
            putString(Edit2of2Fragment.ARG_PROFESION_USUARIO_EDIT_2, p_Usuario?.ProfesionUsuario)
            putString(Edit2of2Fragment.ARG_DESCRIPCION_USUARIO_EDIT_2, p_Usuario?.DescripcionUsuario)
        }

        navhost.findNavController().navigate(R.id.navigation_edit_profile_2of2, bundle)

    }


    private fun GoToVisualizeJob(IdTrabajo: BigInteger){
        //this.tvMenu!!.text = getString(R.string.title_details_jobs)

        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            putString(visualizeJobFragment.ARG_ID_TRABAJO_VISUALIZE_JOB, IdTrabajo.toString())
        }

        navhost.findNavController().navigate(R.id.navigation_details_job, bundle)
    }


    override fun OnClickFragmentButton_Edit2_SaveUser() {
        this.tvMenu!!.text = getString(R.string.title_account)

        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)
        navhost.findNavController().navigate(R.id.navigation_account)

        obtenerImagenAvatarUsuario()
    }

    override fun OnClickRecyclerViewCard_Home_getTrabajo(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)
    }

    override fun OnClickFragmentImage_Home_BusquedaAvanzada() {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        navhost.findNavController().navigate(R.id.navigation_advanced_search)
    }

    override fun OnCliclFragmentButton_SaveOfferJob(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)
    }

    override fun OnClickRecyclerViewCard_MyJobs_getTrabajo(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)

    }

    override fun OnClickRecyclerViewCard_MyApplications_getTrabajo(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)
    }

    override fun OnClickRecyclerViewCard_Notifications_getTrabajo(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)
    }

    fun cerrarSesion() {
        DataManager.progressDialog = ProgressDialog(DataManager.context)
        DataManager.progressDialog!!.setMessage("Saliendo...")
        DataManager.progressDialog!!.setCancelable(false)
        DataManager.progressDialog!!.show()

        val vIntent =  Intent(this, LoginActivity::class.java)
        startActivity(vIntent)

        val sharedPref = LoginActivity.instance?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("IdUsuarioActivo", "")
            apply()
            if(DataManager.progressDialog!!.isShowing) DataManager.progressDialog!!.dismiss()
        }
    }

    override fun OnclickFragmentButton_Edit_EditJob(IdTrabajo: BigInteger) {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            putString(OfferJobFragment.ARG_ID_TRABAJO_OFFER_JOB, IdTrabajo.toString())
        }

        navhost.findNavController().navigate(R.id.navigation_offerJob, bundle)
    }

    override fun OnClickFragmentButton_SeeApplication_Job(IdTrabajo: BigInteger) {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        val bundle = Bundle().apply {
            putString(ApplicationsFragment.ARG_ID_SOLICITUD_VISUALIZE_JOB, IdTrabajo.toString())
        }

        navhost.findNavController().navigate(R.id.navigation_general_applications, bundle)
    }

    override fun OnClickFragmentButton_Apply_Job(IdTrabajo: BigInteger) {
        //TODO("Not yet implemented")

    }

    override fun OnClickFragmentButton_Erase_Jon() {
        var navhost = findViewById<FragmentContainerView>(R.id.nav_host_fragment_activity_menu)

        navhost.findNavController().navigate(R.id.navigation_home)
    }

    override fun networkAvailable() {
        DataManager.subirTrabajoLocalANube()
    }

    override fun networkUnavailable() {
    }

    override fun OnClickRecyclerViewCard_AdvanceSearch_getTrabajo(IdTrabajo: BigInteger) {
        this.GoToVisualizeJob(IdTrabajo)

    }

}