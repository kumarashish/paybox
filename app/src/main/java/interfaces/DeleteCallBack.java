package interfaces;


import model.AddressDataset;

/**
 * Created by ashish.kumar on 02-05-2017.
 */

public interface DeleteCallBack {
    public void onDeleteClick(String addressId);
    public void onEditClick(AddressDataset ds);
    public void onAddresssClick(int pos);
}
