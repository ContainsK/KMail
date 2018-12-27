package com.tk.kmail.project.Main

import android.app.Dialog
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import com.tk.kmail.R
import com.tk.kmail.base.BaseActivity
import com.tk.kmail.base.BaseFragment
import com.tk.kmail.base.IBase
import com.tk.kmail.databinding.LayoutAddUserBinding
import com.tk.kmail.model.db_bean.UserBean
import com.tk.kmail.model.utils.BindingUtils
import com.tk.kmail.model.utils.Evs
import com.tk.kmail.model.utils.ToastUtils
import com.tk.kmail.mvp.UserManager
import com.tk.kmail.project.UserManager.View
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.app_bar_main2.*
import kotlinx.android.synthetic.main.nav_header_main2.*

class Main2Activity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    override fun getLayoutId(): Int {
        return R.layout.activity_main2
    }


    override fun initView() {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
//        fragmentBegin = supportFragmentManager.beginTransaction()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_camera)
        onNavigationItemSelected(nav_view.checkedItem!!)
//        Evs.reg(this)


        object : com.tk.kmail.project.Login.View(), IBase.IViewDialog by getViewDialog() {
            override fun loginResult(bean: UserBean?, b: Boolean) {
                Snackbar.make(mContentView!!, "登录：$b", Snackbar.LENGTH_SHORT).show()
                if (b) {
                    tv_name.text = bean?.descrip ?: "无名"
                    tv_email.text = bean?.username ?: "错误数据"
                }


            }

            init {
                Evs.reg(mPresenter)
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(if (nav_view.checkedItem?.itemId == R.id.nav_camera) R.menu.main2 else R.menu.selected, menu)
        return true
    }

//    private lateinit var fragmentBegin: FragmentTransaction


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main2, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_add_user -> {

                Dialog(getThisContext(), R.style.CsDialog).apply {

                    val bind = BindingUtils.bind<LayoutAddUserBinding>(context, R.layout.layout_add_user)
                    bind.user = UserBean()
                    bind.tilPassword.isPasswordVisibilityToggleEnabled = true
                    setContentView(bind.root)
                    show()
                    window.attributes = window.attributes.apply { width = mContentView!!.width }

                    bind.floatingActionButton.setOnClickListener {
                        val str = View::class.java.name
                        val v: UserManager.View = supportFragmentManager.findFragmentByTag(str) as View
                        v.mPresenter.addUser(bind.user!!)
                        dismiss()
                    }

                }
            }
            R.id.action_add -> {
                ToastUtils.show("添加")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fg: BaseFragment? = null
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                fg = View()
                Snackbar.make(mContentView!!, "nav_camera", Snackbar.LENGTH_SHORT).show()
            }
            R.id.nav_gallery -> {
                fg = com.tk.kmail.project.ProjectManager.View()
                Snackbar.make(mContentView!!, "nav_gallery", Snackbar.LENGTH_SHORT).show()

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }

        }
        invalidateOptionsMenu()
        drawer_layout.closeDrawer(GravityCompat.START)
        supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, fg
                ?: View(), fg?.javaClass?.name).commit()
        return true
    }

}
