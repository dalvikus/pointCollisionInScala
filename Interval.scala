package point.collision

case class SubIntervalInfo(inFirstHalf: Boolean, min: Int, max: Int)

class Interval(val min: Int, val max: Int)
{
    if (min > max) {
        throw new IllegalArgumentException("min > max")
    }

    override def toString: String = "[" + min + ", " + max + "]"

    private val points = (max - min) + 1;
    private val pointsInFirstHalf = points / 2;
    /**
        if set, it means
            1) there is only one point in interval,
            so in general it cannot be divided into two sub-intervals,
            2) but if it must be so as combined with other direction,
            the point is contained in the second sub-interval and there is no the first sub-interval
     */
    val hasOnlyOnePoint: Boolean = pointsInFirstHalf == 0
    /**
    it gives the second sub-interval: [m_minOfSecondHalf, m_max] even though there is no the first sub-interval (hasOnlyOnePoint = true)
     */
    val minOfSecondHalf = min + pointsInFirstHalf

    def outOfRange(i: Int): Boolean = i < min || i > max

    /** tell if the first sub-interval contains given index */
    def firstHalfContains(i: Int): Boolean = {
        if (outOfRange(i))
            throw new IndexOutOfBoundsException("i")

        if (hasOnlyOnePoint)
            false
        else
            i < minOfSecondHalf
    }

    /** get information about sub-interval that contains given index */
    def getSubIntervalContains(i: Int): SubIntervalInfo = {
        if (outOfRange(i))
            throw new IndexOutOfBoundsException("i")

        if (hasOnlyOnePoint) {
            SubIntervalInfo(false, min, max)
        } else {
            if (i < minOfSecondHalf)
                SubIntervalInfo(true, min, minOfSecondHalf - 1)
            else
                SubIntervalInfo(false, minOfSecondHalf, max)
        }
    }
}
