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
import ahgpoug.com.specshelper.Objects.GPU;
import ahgpoug.com.specshelper.R;

public class GpuRecyclerAdapter extends RecyclerView.Adapter<GpuRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GPU> values;

    class ViewHolder extends RecyclerView.ViewHolder {
        private boolean expanded;
        private TextView name;
        private TextView cClock;
        private TextView mClock;
        private TextView memorySize;
        private TextView memoryType;
        private TextView bus;
        private TextView process;
        private TextView slots;
        private TextView sli;
        private TextView price;
        private ImageView cart;

        private ViewHolder(View view) {
            super(view);
            expanded = false;
            name = (TextView) view.findViewById(R.id.name);
            cClock = (TextView) view.findViewById(R.id.cClock);
            mClock = (TextView) view.findViewById(R.id.mClock);
            memorySize = (TextView) view.findViewById(R.id.memorySize);
            memoryType = (TextView) view.findViewById(R.id.memoryType);
            bus = (TextView) view.findViewById(R.id.bus);
            process = (TextView) view.findViewById(R.id.process);
            slots = (TextView) view.findViewById(R.id.slots);
            sli = (TextView) view.findViewById(R.id.sli);
            price = (TextView) view.findViewById(R.id.price);
            cart = (ImageView) view.findViewById(R.id.cartImage);
        }
    }

    public GpuRecyclerAdapter(Context context, ArrayList<GPU> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gpu, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!(Globals.cart.getGpuID() == values.get(position).getId()) || Globals.isAdmin)
            holder.cart.setVisibility(View.GONE);
        else
            holder.cart.setVisibility(View.VISIBLE);

        holder.name.setText(values.get(position).getManufacturer() + " " + values.get(position).getCodename());
        holder.cClock.setText("Частота ГП: " + values.get(position).getcClock() + " МГц");
        holder.mClock.setText("Частота видеопамяти: " + values.get(position).getmClock() + " МГц");
        holder.memorySize.setText("Объем видеопамяти: " + values.get(position).getMemorySize() + " Мб");
        holder.memoryType.setText("Тип памяти: " + values.get(position).getMemoryType());
        holder.bus.setText("Шина: " + values.get(position).getBus() + " бит");
        holder.process.setText("Техпроцесс: " + values.get(position).getProcess() + " нм");
        holder.slots.setText("Занимает слотов: " + values.get(position).getSlots());

        if (values.get(position).isSli())
            holder.sli.setText("Поддержска SLI/CrossFire: да");
        else
            holder.sli.setText("Поддержска SLI/CrossFire: нет");

        holder.price.setText(values.get(position).getPrice() + " руб.");

        if (!holder.expanded){
            holder.memoryType.setVisibility(View.GONE);
            holder.bus.setVisibility(View.GONE);
            holder.process.setVisibility(View.GONE);
            holder.slots.setVisibility(View.GONE);
            holder.sli.setVisibility(View.GONE);
        }

        holder.name.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.expanded){
                    holder.memoryType.setVisibility(View.VISIBLE);
                    holder.bus.setVisibility(View.VISIBLE);
                    holder.process.setVisibility(View.VISIBLE);
                    holder.slots.setVisibility(View.VISIBLE);
                    holder.sli.setVisibility(View.VISIBLE);
                    holder.expanded = true;
                } else {
                    holder.memoryType.setVisibility(View.GONE);
                    holder.bus.setVisibility(View.GONE);
                    holder.process.setVisibility(View.GONE);
                    holder.slots.setVisibility(View.GONE);
                    holder.sli.setVisibility(View.GONE);
                    holder.expanded = false;
                }
            }
        });

        if (!Globals.isAdmin){
            holder.name.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!(Globals.cart.getGpuID() == values.get(position).getId())) {
                        MaterialDialog dialog;
                        MaterialDialog.SingleButtonCallback callback = new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getView();
                                EditText amountET = (EditText) view.findViewById(R.id.amountET);

                                Globals.cart.setGpuID(values.get(position).getId());
                                if (amountET.getText().toString().isEmpty())
                                    Globals.cart.setGpuAmount(1);
                                else
                                    Globals.cart.setGpuAmount(Integer.parseInt(amountET.getText().toString()));

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
                        Globals.cart.setGpuID(-1);
                        Globals.cart.setGpuAmount(0);

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
                    String query = "DELETE FROM gpu WHERE id = " + values.get(position).getId();

                    DataBaseHelper mDatabaseHelper = new DataBaseHelper(context);
                    SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

                    db.execSQL(query);

                    if ((Globals.cart.getGpuID() == values.get(position).getId()))
                        Globals.cart.setGpuID(-1);

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