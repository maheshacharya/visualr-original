<#include "../include/imports.ftl">

<#-- @ftlvariable name="document" type="org.hippoecm.hst.content.beans.standard.HippoGalleryImageSetBean" -->

<#if document??>

<@hst.link var="img" hippobean=document.original/>
    <img src="${img}" title="${document.fileName?html}" alt="${document.fileName?html}"  style="max-width:100%"/>
<#-- @ftlvariable name="editMode" type="java.lang.Boolean"-->
<#elseif editMode>
    <img src="<@hst.link path="/images/essentials/catalog-component-icons/image.png"/>"> Click to edit Image
</#if>
