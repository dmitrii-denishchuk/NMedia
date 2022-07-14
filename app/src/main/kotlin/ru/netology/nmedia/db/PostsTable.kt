package ru.netology.nmedia.db

object PostsTable {

    const val NAME = "posts"

    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.POST_HEADER.columnName} TEXT NON NULL,
            ${Column.DATE.columnName} TEXT NON NULL,
            ${Column.MESSAGE.columnName} TEXT NON NULL,
            ${Column.IS_LIKED.columnName} BOOLEAN NON NULL DEFAULT 0,
            ${Column.LIKES.columnName} INTEGER NON NULL DEFAULT 0,
            ${Column.SHARES.columnName} INTEGER NON NULL DEFAULT 0,
            ${Column.VIEWS.columnName} INTEGER NON NULL DEFAULT 0,
            ${Column.VIDEO.columnName} TEXT NON NULL
        );
    """.trimIndent()

    val ALL_COLUMNS_NAMES = Column.values().map {
        it.columnName
    }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        POST_HEADER("postHeader"),
        DATE("date"),
        MESSAGE("message"),
        IS_LIKED("isLiked"),
        LIKES("likes"),
        SHARES("shares"),
        VIEWS("views"),
        VIDEO("video")
    }
}