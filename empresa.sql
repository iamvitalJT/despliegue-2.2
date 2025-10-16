-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 16-10-2025 a las 16:21:42
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `empresa`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

CREATE TABLE `empleados` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `cargo` varchar(100) NOT NULL,
  `salario` decimal(10,2) NOT NULL,
  `email` varchar(150) NOT NULL,
  `apellido` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id`, `nombre`, `cargo`, `salario`, `email`, `apellido`) VALUES
(1, 'Juan Pérez', 'Desarrollador Senior', 45000.00, 'juan.perez@empresa.com', NULL),
(2, 'Andres', 'Manager', 30000.00, 'andres@correo.com', NULL),
(3, 'María González', 'Gerente de Desarrollo', 85000.00, 'maria.gonzalez@empresa.com', NULL),
(4, 'Carlos Rodríguez', 'Arquitecto de Software', 75000.00, 'carlos.rodriguez@empresa.com', NULL),
(5, 'Ana Martínez', 'Diseñadora UX/UI', 62000.00, 'ana.martinez@empresa.com', NULL),
(6, 'Luis Fernández', 'DevOps Engineer', 68000.00, 'luis.fernandez@empresa.com', NULL),
(7, 'Sofia Herrera', 'Analista de Sistemas', 58000.00, 'sofia.herrera@empresa.com', NULL),
(8, 'Diego Torres', 'Tester QA', 52000.00, 'diego.torres@empresa.com', NULL),
(9, 'Isabella López', 'Scrum Master', 70000.00, 'isabella.lopez@empresa.com', NULL),
(10, 'Alejandro Silva', 'Desarrollador Backend', 60000.00, 'alejandro.silva@empresa.com', NULL),
(11, 'Camila Ruiz', 'Desarrolladora Frontend', 58000.00, 'camila.ruiz@empresa.com', NULL),
(12, 'Roberto Díaz', 'Administrador de BD', 65000.00, 'roberto.diaz@empresa.com', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `activo` bit(1) NOT NULL,
  `fecha_actualizacion` datetime(6) DEFAULT NULL,
  `fecha_creacion` datetime(6) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('ADMIN','EMPLEADO','SUPERVISOR') NOT NULL,
  `reset_token` varchar(255) DEFAULT NULL,
  `reset_token_expiry` datetime(6) DEFAULT NULL,
  `username` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `activo`, `fecha_actualizacion`, `fecha_creacion`, `nombre`, `password`, `rol`, `reset_token`, `reset_token_expiry`, `username`) VALUES
(2, b'1', '2025-10-15 22:55:13.000000', '2025-10-15 22:55:13.000000', 'Administrador del Sistema', '$2a$10$URFULZbeN66nXOhP4hljZ.cBMtkB3Re8Sq5VGZWUZXKDJMRG41fAe', 'ADMIN', NULL, NULL, 'admin'),
(3, b'1', '2025-10-15 22:55:13.000000', '2025-10-15 22:55:13.000000', 'Supervisor de Proyectos', '$2a$10$LgJ7n7oEZZ48PmnIRIkD3OrpTpbZVowHnfipWqWl3B09DjQKaGlbO', 'SUPERVISOR', NULL, NULL, 'supervisor');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `empleados`
--
ALTER TABLE `empleados`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `idx_username` (`username`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `empleados`
--
ALTER TABLE `empleados`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
