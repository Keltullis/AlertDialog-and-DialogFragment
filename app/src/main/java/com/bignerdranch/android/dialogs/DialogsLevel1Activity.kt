package com.bignerdranch.android.dialogs

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.bignerdranch.android.dialogs.databinding.ActivityLevel1Binding
import com.bignerdranch.android.dialogs.entities.AvailableVolumeValues
import kotlin.properties.Delegates.notNull

class DialogsLevel1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLevel1Binding
    private var volume by notNull<Int>()
    private var color by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel1Binding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        binding.showDefaultAlertDialogButton.setOnClickListener {
            showAlertDialog()
        }

        binding.showSingleChoiceAlertDialogButton.setOnClickListener {
            showSingleChoiceAlertDialog()
        }

        binding.showSingleChoiceWithConfirmationAlertDialogButton.setOnClickListener {
            showSingleChoiceWithConfirmationAlertDialog()
        }

        binding.showMultipleChoiceAlertDialogButton.setOnClickListener {
            showMultipleChoiceAlertDialog()
        }

        binding.showMultipleChoiceWithConfirmationAlertDialogButton.setOnClickListener {
            showMultipleChoiceWithConfirmationAlertDialog()
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        color = savedInstanceState?.getInt(KEY_COLOR) ?: Color.RED

        updateUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_VOLUME, volume)
        outState.putInt(KEY_COLOR, color)
    }

    // 2 аргумента: сам диалог и which
    // проверяем какая кнопка была нажата и действуем
    private fun showAlertDialog() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> showToast(R.string.uninstall_confirmed)
                DialogInterface.BUTTON_NEGATIVE -> showToast(R.string.uninstall_rejected)
                DialogInterface.BUTTON_NEUTRAL -> {
                    showToast(R.string.uninstall_ignored)
                }
            }
        }

        // val dialog = AlertDialog.Builder(this)
        //            .setCancelable(false)
        //            .setIcon(R.drawable.ic_baseline_local_fire_department_24)
        //            .setTitle(R.string.default_alert_title)
        //            .setMessage(R.string.default_alert_message)
        //            .setPositiveButton(R.string.action_yes, listener)
        //            .setNegativeButton(R.string.action_no, listener)
        //            .setNeutralButton(R.string.action_ignore, listener)
        //            .create()
        //        dialog.show()

        // Создание и запуск диалога,тут всё очень просто
        // используем билдер,задаём возможность отменить диалог
        // задаём иклнку,заголовок,сообщение и слушатели на кнопки
        // в итоге нужно создать и показать

        val dialog = AlertDialog.Builder(this)
            .setCancelable(true)
            .setIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setTitle(R.string.default_alert_title)
            .setMessage(R.string.default_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .setOnCancelListener {
                showToast(R.string.dialog_cancelled)
            }
            .setOnDismissListener {
                Log.d(TAG, "Dialog dismissed")
            }
            .create()

        dialog.show()
    }

    //.setOnDismissListener срабатывает всегда при закрытии диалога
    //.setOnCancelListener срабатывает только на отмену диалога(кнопка назад)

    private fun showToast(@StringRes messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }

    // ---------
    // формируем список,конвертируем в массив строк
    // в билдере указываем .setSingleChoiceItems,передаём массив строк и текущую выбранную позицию
    // меняем значение позиции по индексу (which),обновляем текствью
    // закрываем диалог dialog.dismiss()

    private fun showSingleChoiceAlertDialog() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_description, it) }
            .toTypedArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex) { dialog, which ->
                volume = volumeItems.values[which]
                updateUi()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    // ----------
    // тоже самое но с подстверждением
    // диалог приводим к AlertDialog,в нём получаем листвью и находим индекс выбранного элемента
    // по индексу вытаскиваем элемент и обновляем

    private fun showSingleChoiceWithConfirmationAlertDialog() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val volumeTextItems = volumeItems.values
            .map { getString(R.string.volume_description, it) }
            .toTypedArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setSingleChoiceItems(volumeTextItems, volumeItems.currentIndex, null)
            .setPositiveButton(R.string.action_confirm) { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                volume = volumeItems.values[index]
                updateUi()
            }
            .create()
        dialog.show()
    }

    // -------

    // setMultiChoiceItems принимает сами элементы,выбранные элементы(массив булеан) и слушатель нажатий
    // цвет(по умолчанию красный),вытягиваем 3 цвета,если хоть 1 компонент больше 0(значит он выбран),мы говорим что это true и делаем массив
    //

    private fun showMultipleChoiceAlertDialog() {
        val colorItems = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkboxes = colorComponents
            .map { it > 0 }
            .toBooleanArray()

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setMultiChoiceItems(colorItems, checkboxes) { _, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                this.color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2]
                )
                updateUi()
            }
            .setPositiveButton(R.string.action_close, null)
            .create()
        dialog.show()
    }

    // -----
    // цвет будет подтверждён только тогда когда нажмут конфёрм

    private fun showMultipleChoiceWithConfirmationAlertDialog() {
        val colorItems = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkboxes = colorComponents
            .map { it > 0 }
            .toBooleanArray()

        var color: Int = this.color
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setMultiChoiceItems(colorItems, checkboxes) { _, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2]
                )
            }
            .setPositiveButton(R.string.action_confirm) { _, _ ->
                this.color = color
                updateUi()
            }
            .create()
        dialog.show()
    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
        binding.colorView.setBackgroundColor(color)
    }

    companion object {
        @JvmStatic private val TAG = DialogsLevel1Activity::class.java.simpleName
        @JvmStatic private val KEY_VOLUME = "KEY_VOLUME"
        @JvmStatic private val KEY_COLOR = "KEY_COLOR"
    }

}