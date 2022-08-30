package com.webkul.mobikul.odoo.ui.cart

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.webkul.mobikul.odoo.R
import com.webkul.mobikul.odoo.databinding.FragmentDeleteCartItemDialogBinding


class DeleteCartItemDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDeleteCartItemDialogBinding
    private lateinit var deleteProduct : DeleteProduct
    private var sellerPosition: Int = -1
    private var lineId: Int = -1
    private var productPosition: Int = -1

    companion object {

        @JvmStatic
        fun newInstance(deleteProduct: DeleteProduct, sellerID: Int, lineId: Int, position : Int): DeleteCartItemDialogFragment {
            return DeleteCartItemDialogFragment().apply {
                this.sellerPosition = sellerID
                this.lineId = lineId
                this.deleteProduct = deleteProduct
                this.productPosition = position
            }
        }

    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        val layoutInflater = requireActivity().layoutInflater
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_delete_cart_item_dialog, null, false)
        binding.apply {
            btnDeleteProductYes.setOnClickListener {
                deleteProduct.onProductDeleted(sellerPosition, lineId, productPosition)
            }
            btnDeleteProductNo.setOnClickListener {
                dismiss()
            }
        }

        dialogBuilder.setView(binding.root)
        return dialogBuilder.create()
    }




    interface DeleteProduct{
        fun onProductDeleted(sellerPosition: Int, lineId: Int, productPosition : Int)
    }



}