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
    private var storedData = ArrayList<Album>()

    private var mContext: Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentStoredmusicBinding.inflate(inflater, container, false)


        storedData.apply{
            add(Album("Savage", "에스파", R.drawable.savage))
            add(Album("쉬어", "그레이노마", R.drawable.showme))
            add(Album("Strawberry moon", "아이유(IU)", R.drawable.strawberry))
            add(Album("낙하", "AKMU", R.drawable.nakha))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
            add(Album("Savage", "에스파", R.drawable.savage))
            add(Album("쉬어", "그레이노마", R.drawable.showme))
            add(Album("Strawberry moon", "아이유(IU)", R.drawable.strawberry))
            add(Album("낙하", "AKMU", R.drawable.nakha))
            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유(IU)", R.drawable.img_album_exp2))
        }

       val lockerRVAdapter=LockerRVAdapter(storedData)
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