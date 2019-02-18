package com.tk.kmail.project.ProjectManager

import com.kmail.greendao.gen.ClassBeanDao
import com.kmail.greendao.gen.MsgBeanDao
import com.tk.kmail.App
import com.tk.kmail.model.db_bean.ClassBean
import com.tk.kmail.model.db_bean.Flag
import com.tk.kmail.model.mails.Mails
import com.tk.kmail.model.utils.NetUtils
import com.tk.kmail.mvp.ProjectMng
import com.tk.kmail.mvp.ProjectMng.Companion.TYPE_PROJECT_ADD
import com.tk.kmail.mvp.ProjectMng.Companion.TYPE_PROJECT_DELETE
import com.tk.kmail.mvp.base.ResultBean

/**
 * Created by TangKai on 2018/12/27.
 */
class Presenter(override val mView: ProjectMng.View) : ProjectMng.Presenter {

    override fun getFolderList(): MutableList<ClassBean> {
        var list = mutableListOf<ClassBean>()
        if (NetUtils.isNetworkAvailable()) {
            val email = App.mails!!
            email.getFolderList().forEach {
                if (it.name.startsWith(Mails.PROJECT_NAME_S))
                    list.add(ClassBean().apply {
                        name = it.name
                        folder = it

                        App.daoSession.classBeanDao.queryBuilder().where(ClassBeanDao.Properties.Name.eq(name))
                                .buildDelete().executeDeleteWithoutDetachingEntities()
                        App.daoSession.classBeanDao.save(this)
                    })
            }
        } else {
            list = App.daoSession.classBeanDao.queryBuilder().where(ClassBeanDao.Properties.Flag.notEq(Flag.FLAG_DELETE)).list()
        }
        return list
    }

    override fun createFolder(nameX: String) {
        val clsDao = App.daoSession.classBeanDao
        val name = Mails.PROJECT_NAME_S + nameX
        mView.runDialog("创建项目中...") {
            if (!NetUtils.isNetworkAvailable()) {
                val ls = clsDao.queryBuilder()
                        .where(ClassBeanDao.Properties.Name.eq(name)).limit(1).list()
                if (ls.size < 1) {
                    App.daoSession.classBeanDao.insert(ClassBean().apply {
                        this.name = name
                        flag = Flag.FLAG_CREATE
                    })
                    return@runDialog true
                }
                return@runDialog false
            }


            val b = App.mails!!.openFolder(name).exists()
            if (b) {
                App.daoSession.classBeanDao.queryBuilder().where(ClassBeanDao.Properties.Name.eq(name))
                        .buildDelete().executeDeleteWithoutDetachingEntities()
                App.daoSession.classBeanDao.insert(ClassBean().apply { this.name = name })
            }
            b
        }.doOnNext { mView.callResult(ResultBean(TYPE_PROJECT_ADD, it, null)) }
                .doOnError {
                    it.printStackTrace()
                    mView.callResult(ResultBean(TYPE_PROJECT_ADD, false, null))
                }.subscribe()
    }

    override fun deleteFolder(name: String) {
        val clsDao = App.daoSession.classBeanDao
        val msgDao = App.daoSession.msgBeanDao

        mView.runDialog("删除项目中...") {
            if (!NetUtils.isNetworkAvailable()) {
                msgDao.queryBuilder()
                        .where(MsgBeanDao.Properties.ClassName.eq(name)).list()
                        .forEach {
                            if (it.flag == Flag.FLAG_DEFAULT) {
                                it.flag = Flag.FLAG_DELETE
                                msgDao.update(it)
                            } else if (it.flag == Flag.FLAG_CREATE) {
                                msgDao.delete(it)
                            }
                        }

                clsDao.queryBuilder()
                        .where(ClassBeanDao.Properties.Name.eq(name)).list()
                        .forEach {
                            if (it.flag == Flag.FLAG_DEFAULT) {
                                it.flag = Flag.FLAG_DELETE
                                clsDao.update(it)
                            } else if (it.flag == Flag.FLAG_CREATE) {
                                clsDao.delete(it)
                            }
                        }
                return@runDialog true
            }

            val f = App.mails!!.openFolder(name)
            if (f != null) {
                App.mails!!.deleteFolder(f)

                msgDao.queryBuilder().where(MsgBeanDao.Properties.ClassName.eq(name))
                        .buildDelete().executeDeleteWithoutDetachingEntities()

                clsDao.queryBuilder().where(ClassBeanDao.Properties.Name.eq(name))
                        .buildDelete().executeDeleteWithoutDetachingEntities()
            }
            true
        }.doOnNext { mView.callResult(ResultBean(TYPE_PROJECT_DELETE, it, null)) }
                .doOnError {
                    it.printStackTrace()
                    mView.callResult(ResultBean(TYPE_PROJECT_DELETE, false, null))
                }.subscribe()
    }


    override fun refreshList() {
        mView.runDialog {
            getFolderList()
        }.subscribe {
            mView.refreshList(it)
        }
    }


}