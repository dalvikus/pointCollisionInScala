import point.collision.{Interval, Node}

object TestNode {
    def main(args: Array[String]) = {
/*
        val H_MAX: Int = 2
        val V_MAX: Int = 3
 */
/*
        val H_MAX: Int = 3
        val V_MAX: Int = 2
 */
        val H_MAX: Int = 16
        val V_MAX: Int = 16
        val a: Array[Array[Boolean]] = Array(
/*
            "01",
            "11",
            "11"
 */
/*
            "011",
            "011"
 */
            "1111100010000001",
            "1111100000000001",
            "0011110011000001",
            "0001111011000001",
            "0001111100000001",
            "0000111110000001",
            "0001111110000001",
            "0001111100000001",
            "0001100001000001",
            "0000000001000001",
            "0000000001000001",
            "0000000001000001",
            "0000000001000001",
            "0000000001000001",
            "0000000001000001",
            "0000000001000001"
        ).map(s => s.toArray.map(e => e != '0'))
        val rootNode: Node = new Node(new Interval(0, H_MAX - 1), new Interval(0, V_MAX - 1))
        for (i <- 0 until V_MAX) {
            for (j <- 0 until H_MAX) {
                if (a(i)(j))
                    rootNode.add(j, i)
            }
        }
        val b = Array.ofDim[Boolean](V_MAX, H_MAX)
        for (i <- 0 until V_MAX) {
            for (j <- 0 until H_MAX) {
                b(i)(j) = rootNode.contains(j, i)
            }
        }
        for (i <- 0 until V_MAX) {
            for (j <- 0 until H_MAX) {
                b(i)(j) = rootNode.contains(j, i)
                assert(a(i)(j) == b(i)(j))
            }
        }

        rootNode.simplify
        val c = Array.ofDim[Boolean](V_MAX, H_MAX)
        for (i <- 0 until V_MAX) {
            for (j <- 0 until H_MAX) {
                c(i)(j) = rootNode.contains2(j, i)
            }
        }
        for (i <- 0 until V_MAX) {
            for (j <- 0 until H_MAX) {
                c(i)(j) = rootNode.contains(j, i)
                assert(a(i)(j) == c(i)(j))
            }
        }

        rootNode.walk(0)
    }
}
