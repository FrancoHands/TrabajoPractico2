1.  ¿Qué responsabilidades asignaste a cada objeto? 
    - A la clase "Paquete" le asigné las responsabilidades de almacenar su propio id, descripción, peso y estado. El paquete controla sus propias transiciones de estado a través de métodos semánticos que representan acciones del negocio: prepararParaEnvio(), marcarEnDistribucion(), entregar() y devolver(). Cada método valida que el estado actual sea el correcto antes de hacer la transición.
    - A la clase "Envio" le asigné la responsabilidad de gestionar sus paquetes (mínimo 1, máximo 3), orquestar las transiciones de estado de sus paquetes llamando a sus métodos semánticos, y calcular el costo total sumando el peso de sus propios paquetes multiplicado por la tarifa del tipo de envío.
    - A las clases "EnvioEstandar", "EnvioExpress" y "EnvioInternacional" les asigné únicamente la responsabilidad de definir su propia tarifa, heredando toda la lógica de Envio.

2.  ¿Qué decisiones te resultaron difíciles? 
    - Me costó entender que cada transición de estado debía ser un método propio del objeto Paquete con nombre semántico, en lugar de un método genérico avanzarEstado(). También entender que el peso es un atributo del paquete y no algo que se pasa externamente al calcular el costo.

3.  ¿Qué métodos decidiste hacer privados y por qué? 
    - Los métodos privados de Envio son "chequearPeso" y "chequearEnvioValido" porque son validaciones internas que sólo necesita la propia clase para asegurar consistencia; no tienen sentido como parte de la interfaz pública.

4.  ¿Qué setters evitaste implementar y por qué?
    - Evité setters para id, descripcion, peso y estado en Paquete. El estado no tiene setter porque cambia únicamente a través de los métodos de negocio (prepararParaEnvio, marcarEnDistribucion, entregar, devolver), que además validan que la transición sea válida. Los demás atributos son inmutables una vez creado el objeto, por lo que no tiene sentido exponerlos.

5.  ¿Qué código tuviste que cambiar al incorporar herencia?
    - Convertí Envio en una clase abstracta con el método abstracto tarifa(). Cada subclase (EnvioEstandar, EnvioExpress, EnvioInternacional) sólo sobreescribe tarifa() para devolver su valor, y toda la lógica de gestión de paquetes y cálculo de costo queda centralizada en Envio.
