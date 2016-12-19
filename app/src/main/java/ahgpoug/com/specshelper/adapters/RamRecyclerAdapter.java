package ahgpoug.com.specshelper.adapters;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import ahgpoug.com.specshelper.util.DataBaseHelper;
import ahgpoug.com.specshelper.util.Globals;
import ahgpoug.com.specshelper.objects.RAM;
import ahgpoug.com.specshelper.R;

public class RamRecyclerAdapter extends RecyclerView.Adapter<RamRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RAM> values;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView clock;
        private TextView memorySize;
        private TextView type;
        private TextView price;
        private ImageView cart;

        private ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            clock = (TextView) view.findViewById(R.id.clock);
            memorySize = (TextView) view.findViewById(R.id.memorySize);
            type = (TextView) view.findViewById(R.id.type);
            price = (TextView) view.findViewById(R.id.price);
            cart = (ImageView) view.findViewById(R.id.cartImage);
        }
    }

    public RamRecyclerAdapter(Context context, ArrayList<RAM> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ram, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!(Globals.cart.getRamID() == values.get(position).getId()) || Globals.isAdmin)
            holder.cart.setVisibility(View.GONE);
        else
            holder.cart.setVisibility(View.VISIBLE);

        holder.name.setText(values.get(position).getManufacturer() + " " + values.get(position).getCodename());
        holder.clock.setText("Частота: " + values.get(position).getClock() + " МГц");
        holder.memorySize.setText("Объем памяти: " + values.get(position).getMemorySize() + " Гб");
        holder.type.setText("Тип памяти: " + values.get(position).getType());
        holder.price.setText(values.get(position).getPrice() + " руб.");

        if (!Globals.isAdmin){
            holder.name.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!(Globals.cart.getRamID() == values.get(position).getId())) {
                        MaterialDialog dialog;
                        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getView();
                                EditText amountET = (EditText) view.findViewById(R.id.amountET);

                                Globals.cart.setRamID(values.get(position).getId());
                                if (amountET.getText().toString().isEmpty())
                                    Globals.cart.setRamAmount(1);
                                else
                                    Globals.cart.setRamAmount(Integer.parseInt(amountET.getText().toString()));

                                Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_LONG).show();
                                notifyDataSetChanged();
                            }
                        };


                        dialog = new MaterialDialog.Builder(context)
                                .title("Введите количество")
                                .customView(R.layout.prompt_amount_input, true)
                                .positiveText("Ок")
                                .negativeText("Отмена")
                                .onPositive(callback)
                                .cancelable(true)
                                .build();
                        dialog.show();
                    } else {
                        Globals.cart.setRamID(-1);
                        Globals.cart.setRamAmount(0);

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
                    String query = "DELETE FROM ram WHERE id = " + values.get(position).getId();

                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    db.execSQL(query);

                    if ((Globals.cart.getRamID() == values.get(position).getId()))
                        Globals.cart.setRamID(-1);

                    values.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Удалено", Toast.LENGTH_LONG).show();
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