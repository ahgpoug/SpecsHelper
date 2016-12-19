package ahgpoug.com.specshelper.adapters;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.Serializable;
import java.util.ArrayList;

import ahgpoug.com.specshelper.ActivityAddNewCpu;
import ahgpoug.com.specshelper.ActivityAddNewMotherboard;
import ahgpoug.com.specshelper.util.DataBaseHelper;
import ahgpoug.com.specshelper.util.Globals;
import ahgpoug.com.specshelper.objects.Motherboard;
import ahgpoug.com.specshelper.R;

public class MotherboardRecyclerAdapter extends RecyclerView.Adapter<MotherboardRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Motherboard> values;

    class ViewHolder extends RecyclerView.ViewHolder {
        private boolean expanded;
        private TextView name;
        private TextView socket;
        private TextView formFactor;
        private TextView chipSet;
        private TextView ram;
        private TextView io;
        private TextView slicfire;
        private TextView maxEthernet;
        private TextView price;
        private ImageView cart;

        private ViewHolder(View view) {
            super(view);
            expanded = false;
            name = (TextView) view.findViewById(R.id.name);
            socket = (TextView) view.findViewById(R.id.socket);
            formFactor = (TextView) view.findViewById(R.id.formFactor);
            chipSet = (TextView) view.findViewById(R.id.chipSet);
            ram = (TextView) view.findViewById(R.id.ram);
            io = (TextView) view.findViewById(R.id.io);
            slicfire = (TextView) view.findViewById(R.id.slicfire);
            maxEthernet = (TextView) view.findViewById(R.id.maxEthernet);
            price = (TextView) view.findViewById(R.id.price);
            cart = (ImageView) view.findViewById(R.id.cartImage);
        }
    }

    public MotherboardRecyclerAdapter(Context context, ArrayList<Motherboard> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motherboard, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!(Globals.cart.getMbID() == values.get(position).getId()) || Globals.isAdmin)
            holder.cart.setVisibility(View.GONE);
        else
            holder.cart.setVisibility(View.VISIBLE);

        holder.name.setText(values.get(position).getManufacturer() + " " + values.get(position).getCodename());
        holder.socket.setText("Socket: " + values.get(position).getSocket());
        holder.formFactor.setText("Форм-фактор: " + values.get(position).getFormFactor());
        holder.chipSet.setText("Чипсет: " + values.get(position).getChipSet());
        holder.ram.setText(values.get(position).getMaxRamCount() + " слота " + values.get(position).getRamType() + ", " + values.get(position).getMaxRamClock() + " МГц, " + values.get(position).getMaxRamSize() + " Гб");
        holder.io.setText("ide: " + values.get(position).getIdeCount() + "\nSATA 6Гбит/с: " + values.get(position).getSata6count() + "\nSATA 3Гбит/с: " + values.get(position).getSata3count() + "\nPCI-E x16: " + values.get(position).getPcie16count() + "\nPCI-E x1: " + values.get(position).getPcie1count() + "\nUSB 2.0: " + values.get(position).getUsb2count() + "\nUSB 3.0: " + values.get(position).getUsb3count());

        String sli = "нет";
        String crossFire = "нет";
        if (values.get(position).isSli())
            sli = "да";
        if (values.get(position).isCrossFire())
            crossFire = "да";
        holder.slicfire.setText("SLI: " + sli + ", CrossFire: " + crossFire);
        holder.maxEthernet.setText("Ethernet: " + values.get(position).getMaxEthernetSpeed() + " Мбит/с");

        holder.price.setText(values.get(position).getPrice() + " руб.");

        if (!holder.expanded){
            holder.ram.setVisibility(View.GONE);
            holder.io.setVisibility(View.GONE);
            holder.slicfire.setVisibility(View.GONE);
            holder.maxEthernet.setVisibility(View.GONE);
        }

        holder.name.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.expanded){
                    holder.ram.setVisibility(View.VISIBLE);
                    holder.io.setVisibility(View.VISIBLE);
                    holder.slicfire.setVisibility(View.VISIBLE);
                    holder.maxEthernet.setVisibility(View.VISIBLE);
                    holder.expanded = true;
                } else {
                    holder.ram.setVisibility(View.GONE);
                    holder.io.setVisibility(View.GONE);
                    holder.slicfire.setVisibility(View.GONE);
                    holder.maxEthernet.setVisibility(View.GONE);
                    holder.expanded = false;
                }
            }
        });

        if (!Globals.isAdmin){holder.name.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!(Globals.cart.getMbID() == values.get(position).getId())) {
                    Globals.cart.setMbID(values.get(position).getId());

                    Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                } else {
                    Globals.cart.setMbID(-1);

                    Toast.makeText(context, "Удалено из корзины", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                }
                return false;
            }
        });
        } else {
            holder.name.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new MaterialDialog.Builder(context)
                            .title("Действия")
                            .items(new String[] {"Изменить", "Удалить"})
                            .itemsCallback(new MaterialDialog.ListCallback() {
                                @Override
                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                    if (text.equals("Удалить")){
                                        String query = "DELETE FROM motherboard WHERE id = " + values.get(position).getId();

                                        DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
                                        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                                        db.execSQL(query);

                                        if ((Globals.cart.getMbID() == values.get(position).getId()))
                                            Globals.cart.setMbID(-1);

                                        values.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Удалено", Toast.LENGTH_LONG).show();
                                    } else {
                                        Intent intent = new Intent(context, ActivityAddNewMotherboard.class);
                                        intent.putExtra("mb", (Serializable) values.get(position));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);

                                        if ((Globals.cart.getMbID() == values.get(position).getId()))
                                            Globals.cart.setMbID(-1);
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}