package edu.ProyectoFinal.Controladores;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import edu.ProyectoFinal.Dto.UsuarioPerfilDto;
import edu.ProyectoFinal.servicios.PerfilServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ExportPDFUsu")
public class ExportarUsuariosAPDF extends HttpServlet {

	PerfilServicio servicioPerfil = new PerfilServicio();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> datos = servicioPerfil.obtenerUsuariosParaSAdmin();

		if (datos.containsKey("error")) {
			resp.setContentType("text/plain;charset=UTF-8");
			resp.getWriter().write("Error: " + datos.get("error"));
			return;
		}

		@SuppressWarnings("unchecked")
		List<UsuarioPerfilDto> usuarios = (List<UsuarioPerfilDto>) datos.get("listadoUsuariosSAdmin");

		// Configurar respuesta para descarga
		resp.setContentType("application/pdf");
		resp.setHeader("Content-Disposition", "attachment; filename=\"usuarios.pdf\"");

		try {
			// Crear documento PDF
			Document document = new Document(PageSize.A4, 36, 36, 36, 36);
			PdfWriter.getInstance(document, resp.getOutputStream());

			document.open();

			// Título
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			Paragraph title = new Paragraph("Listado de Usuarios", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(20);
			document.add(title);

			// Tabla con columnas: ID, Nombre, Email, Perfil...
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1f, 3f, 4f, 2f });

			// Encabezados
			Stream.of("ID", "Alias", "Correo Electrónico", "Rol").forEach(headerTitle -> {
				PdfPCell header = new PdfPCell();
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				header.setBorderWidth(1);
				header.setPhrase(new Phrase(headerTitle));
				table.addCell(header);
			});

			// Filas de datos
			for (UsuarioPerfilDto u : usuarios) {
				table.addCell(String.valueOf(u.getIdUsu()));
				table.addCell(u.getAliasUsu());
				table.addCell(u.getCorreoElectronicoUsu());
				table.addCell(u.getRolUsu());
			}

			document.add(table);
			document.close();

		} catch (DocumentException e) {
			throw new IOException("Error al generar el PDF", e);
		}
	}
}
