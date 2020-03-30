package dev.voleum.ordermolder.ui.general

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.voleum.ordermolder.MainActivity
import dev.voleum.ordermolder.R
import dev.voleum.ordermolder.models.Document
import dev.voleum.ordermolder.viewmodels.AbstractDocViewModel
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class AbstractDocActivity<D : Document<*>, V : AbstractDocViewModel<D, *, *>> : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var fab: FloatingActionButton
    lateinit var progressLayout: ConstraintLayout

    lateinit var docViewModel: V
    protected lateinit var sectionPagerAdapter: SectionsPagerAdapter

    protected var isCreating = true
    protected var savedWithoutClosing = false

    protected val onPageChangeListener = object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {}

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            if (position == 1) fab.show()
            else {
                fab.hide()
                clearFocus()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCreating = intent.getBooleanExtra(DocListActivity.IS_CREATING, true)
    }

    override fun onBackPressed() {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->

            when (which) {
                DialogInterface.BUTTON_NEUTRAL -> dialog.cancel()
                DialogInterface.BUTTON_NEGATIVE -> {
                    if (savedWithoutClosing) {
                        setResultForFinish()
                    }
                    finish()
                }
                DialogInterface.BUTTON_POSITIVE -> {
                    if (docViewModel.table!!.isEmpty())
                        Snackbar.make(fab, R.string.snackbar_empty_goods_list, Snackbar.LENGTH_SHORT)
                                .show()
                    else {
                        docViewModel.saveDoc(docViewModel.document)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(object : CompletableObserver {

                                    override fun onComplete() {
                                        setResultForFinish()
                                        finish()
                                    }

                                    override fun onSubscribe(d: Disposable) {
                                        clearFocus()
                                        showProgressLayout()
                                    }

                                    override fun onError(e: Throwable) {
                                        Log.e(MainActivity.LOG_TAG, e.message.toString())
                                    }
                                })
                    }
                }
            }
        }

        AlertDialog.Builder(this)
                .setMessage(R.string.dialog_save_doc)
                .setPositiveButton(R.string.dialog_yes, dialogClickListener)
                .setNegativeButton(R.string.dialog_no, dialogClickListener)
                .setNeutralButton(R.string.dialog_cancel, dialogClickListener)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.doc_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.doc_save -> {
                if (docViewModel.table!!.isEmpty())
                    Snackbar.make(fab, R.string.snackbar_empty_goods_list, Snackbar.LENGTH_SHORT)
                            .show()
                else {
                    docViewModel.saveDoc(docViewModel.document)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : CompletableObserver {

                                override fun onComplete() {
                                    savedWithoutClosing = true
                                    Snackbar.make(fab, R.string.snackbar_doc_saved, Snackbar.LENGTH_SHORT)
                                            .show()
                                    progressLayout.visibility = View.GONE
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                                    docViewModel.notifyChange()
                                }

                                override fun onSubscribe(d: Disposable) {
                                    clearFocus()
                                    showProgressLayout()
                                }

                                override fun onError(e: Throwable) {
                                    Log.e(MainActivity.LOG_TAG, e.message.toString())
                                }
                            })
                }

            }
        }
        return true
    }

    protected fun setResultForFinish() {
        val intentOut = Intent()
        intentOut.putExtra(DocListActivity.DOC, docViewModel.document)
        intentOut.putExtra(DocListActivity.POSITION, intent.getIntExtra(DocListActivity.POSITION, -1))
        setResult(if (isCreating) DocListActivity.RESULT_CREATED else DocListActivity.RESULT_SAVED, intentOut)
    }

    protected fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showProgressLayout() {
        progressLayout.alpha = 0.0f
        progressLayout.visibility = View.VISIBLE
        progressLayout
                .animate()
                .alpha(1.0f)
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun clearFocus() {
        val focusedView = currentFocus
        focusedView?.clearFocus()
        val imm = viewPager.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(viewPager.windowToken, 0)
    }
}