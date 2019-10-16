package gmedia.net.id.gmediaticketingapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.leonardus.irfan.bluetoothprinter.NotaPrinter;
import com.leonardus.irfan.bluetoothprinter.PrintFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TicketingPrinter extends NotaPrinter {
    public TicketingPrinter(Context context) {
        super(context);
    }

    public void printBarcode(String event_name, List<Bitmap> listBitmap, String customer_name, Date tanggal){
        if(getBluetoothDevice() == null){
            Toast.makeText(getContext(), "Sambungkan ke device printer terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            for(Bitmap bmp : listBitmap){
                //PROSES CETAK HEADER
                getOutputStream().write(PrintFormatter.DEFAULT_STYLE);
                getOutputStream().write(PrintFormatter.ALIGN_CENTER);
                getOutputStream().write(event_name.getBytes());
                getOutputStream().write(PrintFormatter.NEW_LINE);

                //PROSES CETAK BARCODE
                getOutputStream().write(PrintFormatter.decodeBitmap(bmp));

                //PROSES CETAK FOOTER
                getOutputStream().write(customer_name.getBytes());

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                String currentDateandTime = sdf.format(tanggal);
                getOutputStream().write(PrintFormatter.NEW_LINE);
                getOutputStream().write(String.format("%s\n", currentDateandTime).getBytes());
                getOutputStream().write("Terima Kasih\n".getBytes());

                getOutputStream().write("==============================\n".getBytes());
                getOutputStream().write(PrintFormatter.NEW_LINE);
            }

            getOutputStream().write(PrintFormatter.NEW_LINE);

        } catch (Exception e) {
            Log.e("print_log", e.getMessage());
            e.printStackTrace();
        }
    }
}