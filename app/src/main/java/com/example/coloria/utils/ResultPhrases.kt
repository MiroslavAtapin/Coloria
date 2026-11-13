package com.example.coloria.utils

/**
 * Encouraging phrases shown under the similarity percentage on the result stage.
 *
 * The phrases are grouped into bands by [colorSimilarityPercent] value. A band is picked by
 * scanning from the best score downwards, then one phrase is chosen at random inside it.
 *
 * Call sites must remember the returned phrase (keyed by the percentage) so that plain
 * recompositions do not reroll it.
 */
fun randomResultPhrase(percent: Int): String = when {
    percent >= 95 -> PerfectPhrases
    percent >= 85 -> GreatPhrases
    percent >= 75 -> GoodPhrases
    percent >= 60 -> DecentPhrases
    percent >= 40 -> AveragePhrases
    else -> HardPhrases
}.random()

/** 95–100% — practically perfect. */
private val PerfectPhrases = listOf(
    "Вот это глазомер!",
    "Да ты чувствуешь цвет!",
    "Попадание в самое сердце!",
    "Невероятная точность!",
    "Цвет тебя не обманет.",
    "Просто безупречно!",
    "Ты и цвет — одно целое.",
    "Это было практически идеально!",
)

/** 85–94% — very close. */
private val GreatPhrases = listOf(
    "Вау, вот это результат!",
    "Ты явно знаешь толк в цветах!",
    "Очень близко к идеалу!",
    "Да ты настоящий профи!",
    "Вот это меткость!",
    "Почти не отличить!",
    "Красиво сыграно!",
    "У тебя отличный глаз!",
)

/** 75–84% — good. */
private val GoodPhrases = listOf(
    "Ого, неплохо!",
    "Ты на правильном пути!",
    "Хорошее чувство цвета!",
    "Очень даже достойно!",
    "Уже начинаешь видеть оттенки!",
    "Неплохое попадание!",
    "Так держать!",
    "Ещё немного — и идеально!",
)

/** 60–74% — not bad, but room to grow. */
private val DecentPhrases = listOf(
    "Уже что-то получается!",
    "Не так уж далеко!",
    "Хорошая попытка!",
    "Почти поймал оттенок!",
    "Продолжай, получается!",
    "Ещё чуть-чуть точнее!",
    "Есть потенциал!",
    "Цвет оказался хитрее!",
)

/** 40–59% — middle of the road. */
private val AveragePhrases = listOf(
    "Не сдавайся!",
    "Попробуй ещё раз!",
    "Оттенок оказался непростым.",
    "Глаз ещё привыкает!",
    "Не всё так просто, да?",
    "Практика сделает своё дело!",
    "В следующий раз получится лучше!",
    "Цвет немного тебя перехитрил.",
)

/** 0–39% — a tough round. */
private val HardPhrases = listOf(
    "Этот цвет тебя запутал!",
    "Ну, зато было интересно!",
    "Кажется, оттенок победил.",
    "Ничего, попробуем ещё!",
    "Цвет оказался хитрецом!",
    "Не каждый раунд идеален!",
    "Зато теперь ты знаешь больше!",
    "Следующий раунд — твой шанс!",
)