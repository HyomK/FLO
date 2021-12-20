package com.example.flo

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentStoredmusicBinding

class StoredMusicFragment : Fragment() {

    lateinit var binding: FragmentStoredmusicBinding
    private lateinit var storedData :List<Album>

    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentStoredmusicBinding.inflate(inflater, container, false)
        val songDB = SongDatabase.getInstance(mContext!!)!!
        storedData = songDB.albumDao().getAlbums()

        var arr : ArrayList<Song> =ArrayList<Song>();
        arr.addAll(songDB.songDao().getSongs())
        val lockerRVAdapter=LockerRVAdapter(arr)
        binding.storedFrListRv.adapter=lockerRVAdapter
        val dialog = CustomDialog(mContext!!)

        lockerRVAdapter.setLockerItemClickListener(object : LockerRVAdapter.LockerItemClickLister {

            override fun onRemoveSong(position: Int) {
                dialog.showDialog()
                dialog.setOnClickListener(object : CustomDialog.OnDialogClickListener {
                    override fun onClicked(delete :Boolean)
                    {
                        Log.d("RESULT", "###################"+dialog.delete.toString() + "|| ")
                        if(delete){
                            lockerRVAdapter.removeItem(position)
                        }
                    }
                })
            }
        })


        return binding.root
    }


}