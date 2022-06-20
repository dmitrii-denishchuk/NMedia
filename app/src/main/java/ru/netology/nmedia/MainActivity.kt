package ru.netology.nmedia

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            postHeader = "Нетология. Университет интернет-профессий",
            date = LocalDateTime.now()
                .format(
                    DateTimeFormatter
                        .ofLocalizedDateTime(FormatStyle.MEDIUM)
                ),
            isLiked = false
        )

        var countLikes = 1399999
        var countShares = 999_999
        val countViews = "13"

        with(binding) {
            postHeader.text = post.postHeader
            postDate.text = post.date
            likesCount.text = slice(countLikes)
            shareCount.text = slice(countShares)
            viewsCount.text = countViews

            if (post.isLiked) {
                likesButton.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            likesButton.setOnClickListener {
                post.isLiked = !post.isLiked
                likesButton.setImageResource(
                    if (post.isLiked)
                        R.drawable.ic_baseline_favorite_24
                    else
                        R.drawable.ic_baseline_favorite_border_24
                )
                if (post.isLiked) countLikes += 1 else countLikes -= 1
                likesCount.text = slice(countLikes)
            }

            shareButton.setOnClickListener {
                countShares++
                shareCount.text = slice(countShares)
            }
        }
    }
}

fun slice(count: Int): String {
    return when (count) {
        in 1_000..999_999 ->
            if ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000).toString() + "K"
            else ((count.toDouble() / 1_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "K"
        in 1_000_000..999_999_999 ->
            if ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN).toDouble() * 10 % 10 == 0.0)
                (count / 1_000_000).toString() + "M"
            else ((count.toDouble() / 1_000_000).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "M"
        else -> count.toString()
    }
}