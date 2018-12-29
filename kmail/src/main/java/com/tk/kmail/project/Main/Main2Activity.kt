package com.tk.kmail.project.Main

import android.app.Dialog
import android.content.Intent
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
import com.tk.kmail.mvp.Login
import com.tk.kmail.mvp.UserManager
import com.tk.kmail.mvp.base.ResultBean
import com.tk.kmail.project.UserManager.View
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.include_appbar.*
import kotlinx.android.synthetic.main.nav_header_main2.*

class Main2Activity : BaseActivity<com.tk.kmail.mvp.base.IBase.View<com.tk.kmail.mvp.base.IBase.Presenter<*>>>(), NavigationView.OnNavigationItemSelectedListener {
    override fun getViewP(): com.tk.kmail.mvp.base.IBase.View<com.tk.kmail.mvp.base.IBase.Presenter<*>> {
        return null!!
    }


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
        val icon = toolbar.navigationIcon
        val listener = {
            val b = supportFragmentManager.backStackEntryCount > 0
            supportActionBar!!.setDisplayHomeAsUpEnabled(b)
            if (!b) {
                toolbar.navigationIcon = icon
                toolbar.setNavigationOnClickListener {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            } else {
                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
            }
        }
        supportFragmentManager.addOnBackStackChangedListener(listener)
        supportFragmentManager.removeOnBackStackChangedListener(listener)

        object : com.tk.kmail.project.Login.View(), IBase.IViewDialog by getViewDialog() {

            init {
                Evs.reg(mPresenter)
            }

            override fun callResult(result: ResultBean) {

                when (result.type) {
                    Login.TYPE_LOGIN -> {
                        Snackbar.make(mContentView!!, "登录 ${result.status}", Snackbar.LENGTH_SHORT).show()
                        if (result.status) {
                            val aResult = result.getAResult<UserBean>()
                            tv_name.text = aResult?.descrip ?: "无名"
                            tv_email.text = aResult?.username ?: "错误数据"
                            return
                        }
                    }
                    Login.TYPE_OUT -> {
                        tv_name.text = "未登录"
                        tv_email.text = "未登录"
                    }

                }


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
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
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
                    val b = UserBean()
                    b.username = "fgbqahzuk46430@qq.com"
                    b.password = "xfdtpevgqgvpjdeg"
                    bind.user = b
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
                startActivity(Intent(getThisContext(), com.tk.kmail.project.Message.Add.View::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fg: BaseFragment<*>? = null
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
                fg = View()
            }
            R.id.nav_gallery -> {
                fg = com.tk.kmail.project.ProjectManager.View()

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
        for (v in 1..supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStack()
        }
        supportFragmentManager.beginTransaction().replace(R.id.layout_fragment, fg
                ?: View(), fg?.javaClass?.name).commit()
        return true
    }


}
