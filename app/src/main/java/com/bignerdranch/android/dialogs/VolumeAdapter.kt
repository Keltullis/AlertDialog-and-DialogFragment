package com.bignerdranch.android.dialogs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bignerdranch.android.dialogs.databinding.ItemVolumeSingleChoiceBinding

class VolumeAdapter(private val values:List<Int>):BaseAdapter(){

    // Что бы диалог отобразил сложную вью,она должны быть chekable,поэтому была создана checkableLayout
    override fun getView(position: Int, converView: View?, parent: ViewGroup): View {
        val context = parent.context
        val binding = converView?.tag as ItemVolumeSingleChoiceBinding? ?:
        ItemVolumeSingleChoiceBinding.inflate(LayoutInflater.from(context)).also {
            it.root.tag = it
        }

        val volume = getItem(position)

        binding.volumeValueTextView.text = context.getString(R.string.volume_description, volume)
        binding.volumeValueProgressBar.progress = volume
        return binding.root
    }

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): Int {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}