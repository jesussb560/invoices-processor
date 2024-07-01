package com.jesussb.documentservice.document.batch.processors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jesussb.documentservice.document.dto.MailXmlDocument;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DocumentProcessorTest {

    private String xml;
    private List<String> xmls;

    @BeforeEach
    void setUp() {
        xml = """
                <DTE>
                    <Documento ID="DTE_12345678-9_52_100001_X">
                       <Encabezado>
                         <IdDoc>
                           <TipoDTE>52</TipoDTE>
                           <Folio>100001</Folio>
                           <FchEmis>2024-05-28</FchEmis>
                           <IndTraslado>1</IndTraslado>
                         </IdDoc>
                         <Emisor>
                           <RUTEmisor>12345678-9</RUTEmisor>
                           <RznSoc>EMPRESA DE PRUEBA LIMITADA</RznSoc>
                           <GiroEmis>COMERCIO DE PRUEBA</GiroEmis>
                           <Acteco>999999</Acteco>
                           <DirOrigen>CALLE FICTICIA 123</DirOrigen>
                           <CmnaOrigen>COMUNA DE PRUEBA</CmnaOrigen>
                           <CiudadOrigen>CIUDAD DE PRUEBA</CiudadOrigen>
                         </Emisor>
                         <Receptor>
                           <RUTRecep>98765432-1</RUTRecep>
                           <RznSocRecep>CLIENTE DE PRUEBA SPA</RznSocRecep>
                           <GiroRecep>ACTIVIDAD DE PRUEBA</GiroRecep>
                           <DirRecep>AVENIDA INVENTADA 456</DirRecep>
                           <CmnaRecep>OTRA COMUNA</CmnaRecep>
                           <CiudadRecep>OTRA CIUDAD</CiudadRecep>
                         </Receptor>
                         <Totales>
                           <MntNeto>50000</MntNeto>
                           <TasaIVA>19.0</TasaIVA>
                           <IVA>9500</IVA>
                           <MntTotal>59500</MntTotal>
                         </Totales>
                       </Encabezado>
                       <Detalle>
                         <NroLinDet>1</NroLinDet>
                         <CdgItem>
                           <TpoCodigo>ABC</TpoCodigo>
                           <VlrCodigo>12345</VlrCodigo>
                         </CdgItem>
                         <NmbItem>PRODUCTO DE PRUEBA 1</NmbItem>
                         <DscItem>Descripción del producto de prueba 1</DscItem>
                         <QtyItem>10.0</QtyItem>
                         <UnmdItem>UNIDAD</UnmdItem>
                         <PrcItem>1000.0</PrcItem>
                         <MontoItem>10000</MontoItem>
                       </Detalle>
                       <Detalle>
                         <NroLinDet>2</NroLinDet>
                         <CdgItem>
                           <TpoCodigo>DEF</TpoCodigo>
                           <VlrCodigo>67890</VlrCodigo>
                         </CdgItem>
                         <NmbItem>PRODUCTO DE PRUEBA 2</NmbItem>
                         <DscItem>Descripción del producto de prueba 2</DscItem>
                         <QtyItem>20.0</QtyItem>
                         <UnmdItem>UNIDAD</UnmdItem>
                         <PrcItem>2000.0</PrcItem>
                         <MontoItem>40000</MontoItem>
                       </Detalle>
                    </Documento>
                </DTE>
                """;
        xmls = List.of(xml, xml, xml, "test");
    }

    @Test
    void process() {

        var cleanedXmls = xmls.stream()
                .map(this::subStringXml)
                .filter(xml -> !xml.isEmpty())
                .toList();

        assertThat(cleanedXmls.contains("")).isFalse();

        var mappedXmls = cleanedXmls.stream()
                .map(this::subStringXml)
                .flatMap(xml -> xml.isEmpty() ? null : Stream.of((jacksonMapXml(xml))))
                .toList();

        assertThat(mappedXmls.contains(null)).isFalse();
        assertThat(mappedXmls).hasSize(3);

    }

    @Test
    void cleanXml() {

        String finalXmlStructure = subStringXml(xml);

        assertThat(finalXmlStructure).doesNotContain("<DTE");
        assertThat(finalXmlStructure).contains("<Documento");
        assertThat(finalXmlStructure).contains("</Documento>");
        assertThat(finalXmlStructure).doesNotContain("</DTE>");

    }

    @Test
    void mapXml() throws JsonProcessingException {

        XmlMapper mapper = new XmlMapper();
        MailXmlDocument mailXmlDocument = mapper.readValue(subStringXml(xml), MailXmlDocument.class);

        assertThat(mailXmlDocument).isNotNull();
        assertThat(mailXmlDocument.getID()).isEqualTo("DTE_12345678-9_52_100001_X");
        assertThat(mailXmlDocument.getHeader().getIdDoc().getDocType()).isNotNull();
        assertThat(mailXmlDocument.getDetails()).hasSize(2);

    }

    private String subStringXml(String xml) {

        int start = xml.indexOf("<Documento");
        int end = xml.indexOf("</Documento>");

        return (start == -1 || end == -1) ? "" : xml.substring(start, end + "</Documento>".length());

    }

    @SneakyThrows
    private MailXmlDocument jacksonMapXml(String xml) {
        XmlMapper mapper = new XmlMapper();
        return mapper.readValue(xml, MailXmlDocument.class);
    }

}