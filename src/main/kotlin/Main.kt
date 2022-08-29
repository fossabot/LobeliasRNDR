import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.*
import org.openrndr.math.Vector3
import org.openrndr.math.Vector4

fun main() = application {
    configure {
        width = 800
        height = 600
        title = "LobeliasRNDR"
        vsync = true

    }
    program {

        val scrollSensitivty: Double = 2.0

        var obj = RenderObject(5, 18)
        obj.shader = shadeStyle {
            vertexTransform = """
                    va_color = a_color;
                """.trimIndent()
            fragmentTransform = """
                    x_fill = va_color;
                """
        }

        obj.position = Vector3(obj.position.x, obj.position.y, obj.position.z -10.0)

        obj.insertVert(0, Vector3(-1.0,0.0,1.0), Vector4(1.0,0.0,0.0,1.0))
        obj.insertVert(1, Vector3(1.0,0.0,1.0), Vector4(0.0,0.0,1.0,1.0))
        obj.insertVert(2, Vector3(1.0,0.0,-1.0), Vector4(0.0,1.0,0.0,1.0))
        obj.insertVert(3, Vector3(-1.0,0.0,-1.0), Vector4(1.0,0.0,1.0,1.0))
        obj.insertVert(4, Vector3(0.0, 2.0, 0.0), Vector4(0.5, 0.5, 0.5, 1.0))

        obj.insertIndexes(
            shortArrayOf(
                0, 1, 3,
                2, 1, 3,
                3, 4, 2,
                2, 4, 1,
                1, 4, 0,
                0, 4, 3
            )
        )

        mouse.dragged.listen { mse ->

            if (mse.button == MouseButton.CENTER){
                obj.rotation = Vector3(obj.rotation.x + mse.dragDisplacement.y,
                    obj.rotation.y + mse.dragDisplacement.x,
                    obj.rotation.z)
            }
        }

        mouse.scrolled.listen{
            obj.position = Vector3(obj.position.x, obj.position.y,
                obj.position.z + (it.rotation.y * scrollSensitivty))
        }

        extend {
            drawer.perspective(60.0, width * 1.0 / height, 0.01, 2000.0)
            drawer.depthWrite = true
            drawer.depthTestPass = DepthTestPass.LESS_OR_EQUAL

            drawer.shadeStyle = obj.shader
            drawer.translate(obj.position.x, obj.position.y, obj.position.z)
            drawer.rotate(Vector3.UNIT_X, obj.rotation.x)
            drawer.rotate(Vector3.UNIT_Y, obj.rotation.y)
            drawer.vertexBuffer(obj.indexBuff, listOf(obj.vertBuff), DrawPrimitive.TRIANGLES)
        }
    }
}