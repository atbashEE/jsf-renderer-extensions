<?xml version="1.0" encoding="utf-8"?>
<!--

    Copyright 2014-2017 Rudy De Busscher

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:xslthl="http://xslthl.sf.net"
                xmlns:d="http://docbook.org/ns/docbook"
                exclude-result-prefixes="xslthl d"
                version="1.0">
    <!-- import the main stylesheet, here pointing to fo/docbook.xsl -->
    <xsl:import href="urn:docbkx:stylesheet"/>

    <!-- highlight.xsl must be imported in order to enable highlighting support, highlightSource=1 parameter
     is not sufficient -->
    <xsl:import href="urn:docbkx:stylesheet/highlight.xsl"/>

    <xsl:template match='xslthl:keyword' mode="xslthl">
        <fo:inline font-weight="bold" color="blue">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <xsl:template match='xslthl:tag' mode="xslthl">
        <fo:inline font-weight="bold" color="darkblue">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <xsl:template match='xslthl:attribute' mode="xslthl">
        <fo:inline font-style="italic">
            <xsl:apply-templates/>
        </fo:inline>
    </xsl:template>

    <!-- Customized titlepage -->
    <xsl:template name="book.titlepage">
        <xsl:choose>
            <xsl:when test="d:bookinfo/d:title">
                <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:bookinfo/d:title"/>
            </xsl:when>
            <xsl:when test="d:info/d:title">
                <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:title"/>
            </xsl:when>
            <xsl:when test="d:title">
                <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:title"/>
            </xsl:when>
        </xsl:choose>

        <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style"
                  text-align="center" font-size="14pt" space-before="18.6624pt" font-weight="bold"
                  font-family="{$title.fontset}">

            <xsl:for-each select="d:info/d:revhistory/d:revision">
                <fo:block>
                    <xsl:text xml:space="preserve">Version : </xsl:text>
                    <xsl:value-of select="./d:revnumber"/>
                    <xsl:text xml:space="preserve"> - </xsl:text>
                    <xsl:value-of select="./d:date"/>
                </fo:block>
            </xsl:for-each>
        </fo:block>

        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="d:info/d:author"/>

    </xsl:template>

    <!-- Smaller author on title page-->
    <xsl:template match="d:author" mode="book.titlepage.recto.auto.mode">
        <fo:block xmlns:fo="http://www.w3.org/1999/XSL/Format" xsl:use-attribute-sets="book.titlepage.recto.style"
                  font-size="11" space-before="10.8pt" keep-with-next.within-column="always">
            <xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
        </fo:block>
    </xsl:template>

    <!--Boldface emphasis  Not picked up!!-->
    <xsl:template
            match="d:emphasis[@d:role='marked']">
        <xsl:call-template
                name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="processing-instruction('asciidoc-pagebreak')">
        <fo:block break-after='page'/>
    </xsl:template>

    <!-- background for program listing-->
    <xsl:param name="shade.verbatim" select="1"/>

    <xsl:attribute-set name="shade.verbatim.style">
        <xsl:attribute name="background-color">#E0E0E0</xsl:attribute>
        <xsl:attribute name="border-width">0.5pt</xsl:attribute>
        <xsl:attribute name="border-style">solid</xsl:attribute>
        <xsl:attribute name="border-color">#575757</xsl:attribute>
        <xsl:attribute name="padding">3pt</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="monospace.verbatim.properties">
        <xsl:attribute name="wrap-option">wrap</xsl:attribute>
    </xsl:attribute-set>

    <!-- line breaks from ascidoc ( + at tend of line) -->
    <xsl:template match="processing-instruction('asciidoc-br')">
        <fo:block/>
    </xsl:template>

</xsl:stylesheet>
