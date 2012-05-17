<#escape x as x?html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>${doi}</title>
  <link rel="stylesheet" type="text/css" href="${contextPath}/static/main.css" />
  <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
  <#list allMedia as media>
  <link rel="meta" type="${media}" href="${contextPath}/${media}/${doi}" />
  </#list>
  <script type="text/javascript">

    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', 'UA-22806196-2']);
    _gaq.push(['_trackPageview']);

    (function() {
      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

  </script>
</head>

<body>
<div id="header">
<h1>
  <img src="${contextPath}/static/dc-logo.gif" alt="DataCite logo" /> DataCite Content Service <small>Beta</small>
</h1>
<h2>doi:${doi}</h2>
<#if xmlPresent><p>This page represents DataCite's metadata for <em>doi:${doi}</em>.</p></#if>
<p>For a landing page of this dataset please follow
<a href="http://dx.doi.org/${doi}">http://dx.doi.org/${doi}</a></p>
</div>


<#if xmlPresent>

<div id="metadata">

<div class="block">
  <div class="block-label">Citation</div>
  <div class="content">
  <#list creators! as c>${c}; </#list>(${publicationYear!}): ${firstTitle!}; ${publisher!}. http://dx.doi.org/${doi}

  <a title="Export to Reference Manager/EndNote" href="${contextPath}/application/x-research-info-systems/${doi}"><span class="citation-button">RIS</span></a>
  <a title="Export to BibTeX" href="${contextPath}/application/x-bibtex/${doi}"><span class="citation-button">BibTeX</span></a>
  </div>
</div>

<#if (descriptions)?has_content>
  <div class="block">
    <div class="block-label">Descriptions</div>
    <#list descriptions! as d>
      <div class="label">${d.key}</div>
      <div class="content">${d.value}</div>
    </#list>
  </div>
</#if>

<#if (resourceTypes)?has_content>
  <div class="block">
    <div class="block-label">Resource type</div>
    <#list resourceTypes! as d>
          <div class="label">${d.key}&emsp;</div>
          <div class="content">${d.value}&emsp;</div>
    </#list>
  </div>
</#if>

<#if (subjects)?has_content>
  <div class="block">
    <div class="block-label">Subjects</div>
    <#assign more = 0>
    <#list subjects as d>
      <#if (more > 0) || (d.key)?has_content>
        <div class="label">${d.key}&emsp;</div>
      </#if>
      <div class="content">${d.value}</div>
      <#assign more = 1>
    </#list>
  </div>
</#if>

<#if (rights)?has_content>
  <div class="block">
    <div class="block-label">License</div>
    <div class="content">${rights}</div>
  </div>
</#if>

<#if (sizes)?has_content>
  <div class="block">
    <div class="block-label">Size</div>
    <#assign more = 0>
    <#list sizes as d>
      <#if (more > 0)>
        <div class="label">&emsp;</div>
      </#if>
      <div class="content">${d}</div>
      <#assign more = 1>
    </#list>
  </div>
</#if>

<#if (language)?has_content>
  <div class="block">
    <div class="block-label">Language</div>
    <div class="content">${language}</div>
  </div>
</#if>

<#if (dates)?has_content>
  <div class="block">
    <div class="block-label">Dates</div>
    <#list dates! as d>
          <div class="label">${d.key}&emsp;</div>
          <div class="content">${d.value}</div>
    </#list>
  </div>
</#if>

<#if (version)?has_content>
  <div class="block">
    <div class="block-label">Version</div>
    <div class="content">${version}</div>
  </div>
</#if>

<#if (formats)?has_content>
  <div class="block">
    <div class="block-label">Formats</div>
    <#assign more = 0>
    <#list formats as d>
      <#if (more > 0)>
        <div class="label">&emsp;</div>
      </#if>
      <div class="content">${d}</div>
      <#assign more = 1>
    </#list>
  </div>
</#if>

<#if (alternateIdentifiers)?has_content>
  <div class="block">
    <div class="block-label">Alternate identifiers</div>
    <#list alternateIdentifiers! as d>
          <div class="label">${d.key}&emsp;</div>
          <div class="content">${d.value}</div>
    </#list>
  </div>
</#if>

<#if (relatedIdentifiers)?has_content>
  <div class="block">
    <div class="block-label">Related identifiers</div>
    <#list relatedIdentifiers! as d>
          <div class="label">${d.key}&emsp;</div>
          <div class="content">${d.value}</div>
    </#list>
  </div>
</#if>

<#if (contributors)?has_content>
  <div class="block">
    <div class="block-label">Contributors</div>
    <#list contributors! as d>
          <div class="label">${d.key}&emsp;</div>
          <div class="content">${d.value}</div>
    </#list>
  </div>
</#if>

</#if>

<#if (allMedia)?has_content>
    <div class="block">
      <div class="block-label">Other formats</div>
      <#list allMedia as media>
        <div class="label">&emsp;</div>
        <div class="content"><a href="${contextPath}/${media}/${doi}">${media}</a></div>
      </#list>
    </div>
</#if>

</div>
<p>This<#if xmlPresent> metadata</#if> record was deposited by ${datacentreName} in co-operation with ${allocatorName}.</p>
</body>
</html>

</#escape>
