package point.collision

class Node(val hInterval: Interval, val vInterval: Interval)
{
    private var NW: Node = null
    private var NE: Node = null
    private var SW: Node = null
    private var SE: Node = null

    /**
     * tell how many hits are in the smallest node, which contains one one point; 0 for other nodes
     *
     * this is an example of information for the smallest node; not used in current application
     */
    private var hits: Int = 0

    /**
     * add a point specified by h and v to to the node that contains only the point;
     * if necessary, it creates all parent nodes
     */
    def add(h: Int, v: Int): Unit = {
        if (hInterval.outOfRange(h))
            throw new IndexOutOfBoundsException("h")
        if (vInterval.outOfRange(v))
            throw new IndexOutOfBoundsException("v")

        if (hInterval.hasOnlyOnePoint && vInterval.hasOnlyOnePoint) {
            hits = hits + 1
            return
        }
        // at least one of intervals can be divided into two non-empty sub-intervals

        val hSubIntervalInfo: SubIntervalInfo = hInterval.getSubIntervalContains(h)
        val vSubIntervalInfo: SubIntervalInfo = vInterval.getSubIntervalContains(v)
        if (hSubIntervalInfo.inFirstHalf) {
            if (vSubIntervalInfo.inFirstHalf) {
                if (NW == null)
                    NW = new Node(new Interval(hSubIntervalInfo.min, hSubIntervalInfo.max), new Interval(vSubIntervalInfo.min, vSubIntervalInfo.max))
                NW.add(h, v)
            } else {
                if (SW == null)
                    SW = new Node(new Interval(hSubIntervalInfo.min, hSubIntervalInfo.max), new Interval(vSubIntervalInfo.min, vSubIntervalInfo.max))
                SW.add(h, v)
            }
        } else {
            if (vSubIntervalInfo.inFirstHalf) {
                if (NE == null)
                    NE = new Node(new Interval(hSubIntervalInfo.min, hSubIntervalInfo.max), new Interval(vSubIntervalInfo.min, vSubIntervalInfo.max))
                NE.add(h, v)
            } else {
                if (SE == null)
                    SE = new Node(new Interval(hSubIntervalInfo.min, hSubIntervalInfo.max), new Interval(vSubIntervalInfo.min, vSubIntervalInfo.max))
                SE.add(h, v)
            }
        }
    }

    /**
     * tell if all child nodes contains the point specified by h and v
     * once simplified by calling simplify, use contains2 below
     */
    def contains(h: Int, v: Int): Boolean = {
        if (hInterval.outOfRange(h))
            throw new IndexOutOfBoundsException("h")
        if (vInterval.outOfRange(v))
            throw new IndexOutOfBoundsException("v")

        if (hInterval.hasOnlyOnePoint && vInterval.hasOnlyOnePoint) {
            true
        } else {
            val hInFirstHalf: Boolean = hInterval.firstHalfContains(h)
            val vInFirstHalf: Boolean = vInterval.firstHalfContains(v)
            if (hInFirstHalf) {
                if (vInFirstHalf)
                    NW != null && NW.contains(h, v)
                else
                    SW != null && SW.contains(h, v)
            } else {
                if (vInFirstHalf)
                    NE != null && NE.contains(h, v)
                else
                    SE != null && SE.contains(h, v)
            }
        }
    }

    def walk(depth: Int = 0): Unit = {
        val indentStr: String = " " * (2 * depth)
        println(indentStr + hInterval.toString + vInterval.toString)
        println(indentStr + "NW: " + NW)
        if (NW != null)
            NW.walk(depth + 1)
        println(indentStr + "NE: " + NE)
        if (NE != null)
            NE.walk(depth + 1)
        println(indentStr + "SW: " + SW)
        if (SW != null)
            SW.walk(depth + 1)
        println(indentStr + "SE: " + SE)
        if (SE != null)
            SE.walk(depth + 1)
    }

    /** set if all children nodes are occupied */
    private var simplified: Boolean = false

    /** if all child nodes exist, it can be simplified in order to check quickly if a node contains a point */
    def simplify: Unit = {
        if (NW != null)
            NW.simplify
        if (NE != null)
            NE.simplify
        if (SW != null)
            SW.simplify
        if (SE != null)
            SE.simplify

        if (hInterval.hasOnlyOnePoint && vInterval.hasOnlyOnePoint) {
            simplified = true
            return
        }

        // Check if two sub-intervals in h-direction can be simplified
        // There are three cases that can be simplified:
        //  Case 1. NW NE SW SE
        //      a(NW)  b(NE)
        //      c(SW)  d(SE)
        //  Case 2. NE SE; no h-direction
        //      a (NE)
        //      b (SE)
        //  Case 3. SW SE; no v-direction
        //      a(SW) b(SE)
        if (NW != null && NE != null && SW != null && SE != null) { // Case 1
            if (NW.simplified && NE.simplified && SW.simplified && SE.simplified) {
                println(hInterval.toString + vInterval.toString + ": simplified")
                simplified = true
            }
        } else if (NW == null && NE != null && SW == null && SE != null) { // Case 2
            if (
                hInterval.hasOnlyOnePoint &&   // no h-direction
                NE.simplified && SE.simplified
            ) {
                println(hInterval.toString + vInterval.toString + ": simplified")
                simplified = true
            }
        } else if (NW == null && NE == null && SW == null && SE != null) { // Case 3
            if (
                vInterval.hasOnlyOnePoint &&   // no v-direction
                SW.simplified && SE.simplified
            ) {
                println(hInterval.toString + vInterval.toString + ": simplified")
                simplified = true
            }
        }
    }

    /** same as contains but it is better once simplified */
    def contains2(h: Int, v: Int): Boolean = {
        if (simplified)
            true
        else
            contains(h, v)
    }
}
