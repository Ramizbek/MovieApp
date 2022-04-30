package ramizbek.aliyev.movieapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import ramizbek.aliyev.movieapp.Adapter.CinemaAdapter
import ramizbek.aliyev.movieapp.Cache.MySharedPreference
import ramizbek.aliyev.movieapp.Models.Cinema
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var cinemaList: ArrayList<Cinema> = ArrayList()
    lateinit var cinemaAdapter: CinemaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadData()

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        rv.addItemDecoration(dividerItemDecoration)
    }

    private fun loadData() {
        MySharedPreference.init(this)
        cinemaList = MySharedPreference.objectString
        cinemaAdapter =
            CinemaAdapter(this, cinemaList, object : CinemaAdapter.OnMyItemClickListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onMyItemClckDelete(cinema: Cinema, position: Int) {
                    cinemaList = MySharedPreference.objectString
                    cinemaList.removeAt(position)
                    MySharedPreference.objectString = cinemaList
                    println(position)
                    loadData()
                    cinemaAdapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Removed", Toast.LENGTH_SHORT).show()
                }

                override fun onMyItemClckDEdit(cinema: Cinema, position: Int) {
                    val intent = Intent(this@MainActivity, Edit::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onMyItemDelete(cinema: Cinema, position: Int) {
                    cinemaList.removeAt(position)
                    MySharedPreference.objectString = cinemaList
                    cinemaAdapter.notifyItemRemoved(position)
                    cinemaAdapter.notifyItemRangeChanged(
                        position,
                        MySharedPreference.objectString.size
                    )
                }

                override fun onMyItemEdit(cinema: Cinema, position: Int) {
                    val intent = Intent(this@MainActivity, Edit::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

                override fun onMyLinClick(cinema: Cinema, position: Int) {
                    val intent = Intent(this@MainActivity, Avangers::class.java)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

            })
        rv.adapter = cinemaAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onStart() {
        super.onStart()
        loadData()
        cinemaAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i("menu", "menuItem")
        println("Funksiyga kirdi")
        return when (item.itemId) {
            R.id.add_menu -> {
                val intent = Intent(this, AddMovie::class.java)
                startActivity(intent)
                Toast.makeText(this, "Add menu", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
