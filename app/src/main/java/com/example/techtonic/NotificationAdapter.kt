import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.techtonic.Class.NotificationClass
import com.example.techtonic.R

class NotificationAdapter(
    private val context: Context,
    private val notifications: List<NotificationClass>
) : BaseAdapter() {

    override fun getCount(): Int = notifications.size

    override fun getItem(position: Int): Any = notifications[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_notification,
            parent,
            false
        )

        val hazardType: TextView = view.findViewById(R.id.txtHazardType)
        val status: TextView = view.findViewById(R.id.txtStatus)
        val imageView: ImageView = view.findViewById(R.id.imgHazard)

        val notification = notifications[position]

        hazardType.text = notification.hazardType
        status.text = if (notification.status == "Solved") "Resolved" else "Pending"

        val into = Glide.with(context)
            .load(notification.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .apply(RequestOptions().circleCrop())
            .into(imageView)

        return view
    }
}
