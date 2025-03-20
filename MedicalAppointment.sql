/* Creacion de la base de datos */
drop database Medicalappointment;
create database MedicalAppointment;

use MedicalAppointment;

/* Creacion de tablas */

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(20) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL, /* 255 -> puede estar encriptada*/
    rol ENUM('Medico', 'Paciente', 'Administrador') NOT NULL
);

CREATE TABLE medicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    cedula VARCHAR(20)  UNIQUE NOT NULL,
    nombre VARCHAR(30) NOT NULL,
	aprobado BOOLEAN DEFAULT FALSE,
    especialidad VARCHAR(30),
    costo_consulta DECIMAL(10, 2),
    lugar_atencion VARCHAR(80),
    horario_inicio TIME,
    horario_fin TIME,
    frecuencia_citas varchar (20),
    foto_url VARCHAR(255),
    presentacion TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) 
);

CREATE TABLE pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    cedula VARCHAR(20)  UNIQUE NOT NULL,
    nombre VARCHAR(30) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    foto_url VARCHAR(255),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) 
);

CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medico_id INT,
    paciente_id INT,
    fecha_cita DATE,
    hora_cita TIME,
    estado ENUM('Pendiente', 'Confirmada', 'Atendida', 'Cancelada') DEFAULT 'Pendiente',
    anotaciones TEXT,
    FOREIGN KEY (medico_id) REFERENCES medicos(id),
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id)
);

/* Administrador */
insert into usuarios (usuario, clave, rol) 
values ('Admin', 'root', 'Administrador');

/* usuarios medicos */
insert into usuarios (usuario, clave, rol) 
values ('BBanner', '111', 'Medico');
insert into usuarios (usuario, clave, rol) 
values ('JPerez', '222', 'Medico');
insert into usuarios (usuario, clave, rol) 
values ('LKjero', '333', 'Medico');

/* medicos */
insert into medicos (usuario_id, cedula, nombre, aprobado, especialidad, 
costo_consulta, lugar_atencion, horario_inicio, horario_fin, frecuencia_citas,
 foto_url, presentacion) values (1, '111111111', 'Bruce Branner', true, 'Dermatology', 50000, 'Cima Hospital, San Jose',
 '08:00:00', '16:00:00', '30 minutos',  '', 'Médico altamente capacitado');
insert into medicos (usuario_id, cedula, nombre, aprobado, especialidad, 
costo_consulta, lugar_atencion, horario_inicio, horario_fin, frecuencia_citas,
 foto_url, presentacion) values (2, '222222222', 'Juan José Perez', true, 'Cardiology', 60800, 'Medical Center, Alajuela',
 '08:00:00', '16:00:00', '1 horas',  '', 'Médico altamente capacitado en cirugía cardiovascular');
insert into medicos (usuario_id, cedula, nombre, aprobado, especialidad, 
costo_consulta, lugar_atencion, horario_inicio, horario_fin, frecuencia_citas,
 foto_url, presentacion) values (3, '333333333', 'Luis Alfoncso Kajero', true, 'Cardiology', 70000, 'Heart Care Center, Alajuela',
 '11:00:00', '19:00:00', '1 horas',  'https://www.google.com/search?sca_esv=88576adaa55fa00a&sxsrf=AHTn8zrs29HkHKy4YXBLbRB_sDj6lJHc5w:1742488839632&q=gato&udm=2&fbs=ABzOT_BnMAgCWdhr5zilP5f1cnRvK9uZj3HA_MTJAA6lXR8yQElaIApxtef1-RKg2CcwxXZXWlQ6hBCdDT6diQURAAwgUtj_wq4LLUgBoz7X4yTlMSTj86k-ePdnniocOpdLXQWvc6wAnJjTV-v2pT6HYUJZEjf9yBRKxVxZrE5mFrx0d9wqaA9LTeCOL_rQTkCNi3YyzEmCo-P68YIESiVHlrWXLL3G5w&sa=X&ved=2ahUKEwi7v77ijJmMAxU-SDABHYU8JYcQtKgLegQIHxAB&biw=1920&bih=919#vhid=IycprWj_vv_diM&vssid=mosaic', 'Profesional dedicado a la atención');
 
 /* usuarios pacientes */
insert into usuarios (usuario, clave, rol) values ('GLucas', '444', 'Paciente');
insert into usuarios (usuario, clave, rol) values ('SLee', '555', 'Paciente');

 /*Pacientes*/
insert into pacientes (usuario_id, cedula, nombre, telefono, direccion, foto_url)
 values (4, '4444444444', 'Georges Lucas', '555-1234', 'Alajuela', '');
insert into pacientes (usuario_id, cedula, nombre, telefono, direccion, foto_url)
 values (5, '5555555555', 'Stan Lee', '665-1245', 'San Jose', '');
 
 select * from medicos;