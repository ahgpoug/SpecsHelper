package ahgpoug.com.specshelper.adapters;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import ahgpoug.com.specshelper.DataBaseHelper;
import ahgpoug.com.specshelper.Globals;
import ahgpoug.com.specshelper.Objects.CPU;
import ahgpoug.com.specshelper.R;

public class CpuRecyclerAdapter extends RecyclerView.Adapter<CpuRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CPU> values;

    class ViewHolder extends RecyclerView.ViewHolder {
        private boolean expanded;
        private TextView name;
        private TextView socket;
        private TextView coresCount;
        private TextView clock;
        private TextView gpuType;
        private TextView process;
        private TextView tdp;
        private TextView release;
        private TextView price;
        private ImageView cart;

        private ViewHolder(View view) {
            super(view);
            expanded = false;
            name = (TextView) view.findViewById(R.id.name);
            socket = (TextView) view.findViewById(R.id.socket);
            coresCount = (TextView) view.findViewById(R.id.coresCount);
            clock = (TextView) view.findViewById(R.id.clock);
            gpuType = (TextView) view.findViewById(R.id.gpuType);
            process = (TextView) view.findViewById(R.id.process);
            tdp = (TextView) view.findViewById(R.id.tdp);
            release = (TextView) view.findViewById(R.id.release);
            price = (TextView) view.findViewById(R.id.price);
            cart = (ImageView) view.findViewById(R.id.cartImage);
        }
    }

    public CpuRecyclerAdapter(Context context, ArrayList<CPU> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cpu, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!(Globals.cart.getCpuID() == values.get(position).getId()))
            holder.cart.setVisibility(View.GONE);
        else
            holder.cart.setVisibility(View.VISIBLE);

        holder.name.setText(values.get(position).getManufacturer() + " " + values.get(position).getCodename());
        holder.socket.setText("Socket: " + values.get(position).getSocket());
        holder.coresCount.setText("Ядер: " + values.get(position).getCoresCount());
        holder.clock.setText("Частота ядра: " + values.get(position).getClock() + " МГц");

        if (values.get(position).getGpuType().toString().trim().isEmpty())
            holder.gpuType.setVisibility(View.GONE);
        else
            holder.gpuType.setText("GPU: " + values.get(position).getGpuType());

        holder.process.setText("Техпроцесс: " + values.get(position).getProcess() + " нм");
        holder.tdp.setText("Тепловыделение: " + values.get(position).getTdp() + " Вт");
        holder.release.setText("Дата выхода: " + values.get(position).getRelease() + " г.");
        holder.price.setText(values.get(position).getPrice() + " руб.");

        if (!holder.expanded){
            holder.gpuType.setVisibility(View.GONE);
            holder.process.setVisibility(View.GONE);
            holder.tdp.setVisibility(View.GONE);
            holder.release.setVisibility(View.GONE);
        }

        holder.name.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.expanded){
                    if (!values.get(position).getGpuType().toString().trim().isEmpty())
                        holder.gpuType.setVisibility(View.VISIBLE);
                    holder.process.setVisibility(View.VISIBLE);
                    holder.tdp.setVisibility(View.VISIBLE);
                    holder.release.setVisibility(View.VISIBLE);
                    holder.expanded = true;
                } else {
                    holder.gpuType.setVisibility(View.GONE);
                    holder.process.setVisibility(View.GONE);
                    holder.tdp.setVisibility(View.GONE);
                    holder.release.setVisibility(View.GONE);
                    holder.expanded = false;
                }
            }
        });

        if (!Globals.isAdmin){
            holder.name.getRootView().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!(Globals.cart.getCpuID() == values.get(position).getId())) {
                        Globals.cart.setCpuID(values.get(position).getId());

                        Toast.makeText(context, "Добавлено в корзину", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    } else {
                        Globals.cart.setCpuID(-1);

                        Toast.makeText(context, "Удалено из корзины", Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();
                    }
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