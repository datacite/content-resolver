<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
  <title>${doi}</title>
  <link rel="stylesheet" type="text/css" href="../static/main.css" />
  <#list allMedia as media>
  <link rel="meta" type="${media}" href="/${media}/${doi}" />
  </#list>
</head>

<body>
<div id="header">
<h1>
  <img src="../static/dc-logo.gif" alt="DataCite logo" /> DataCite Content Service <small>Beta</small>
</h1>
<h2>doi:${doi}</h2>
<p>This page represents DataCite's metadata for <em>doi:${doi}</em>.
For a landing page of this dataset please follow
<a href="http://dx.doi.org/${doi}">http://dx.doi.org/${doi}</a></p>
</div>

<div id="metadata">

<div class="block">
  <div class="block-label">Citation</div>
  <div class="content">
  <#list creators as c>${c}; </#list>(${publicationYear}): <#list titles?values as t>${t}; </#list>${publisher}. http://dx.doi.org/${doi}

  <a title="Export to Reference Manager/EndNote" href="/application/x-research-info-systems/${doi}"><span class="citation-button">RIS</span></a>
  <a title="Export to BibTeX" href="/application/x-bibtex/${doi}"><span class="citation-button">BibTeX</span></a>
  </div>
</div>

<#if descriptions??>
  <div class="block">
    <div class="block-label">Descriptions</div>
    <#list descriptions?keys as d>
      <div class="label">${d}</div>
      <div class="content">${descriptions[d]}</div>
    </#list>
  </div>
</#if>

<#if resourceType?? >
  <div class="block">
    <div class="block-label">Resource type</div>
    <#list descriptions?keys as d>
          <div class="label">${d}&emsp;</div>
          <div class="content">${descriptions[d]}</div>
    </#list>
  </div>
</#if>

<#--

<% unless @meta.subjects.nil? || @meta.subjects.empty?  %>
  <div class="block">
    <div class="block-label">Subjects</div>
    <% idx = 0 %>
    <% for d in @meta.subjects %>
      <% if idx > 0 || (!d[:type].nil? && !d[:type].empty?) %>
        <div class="label"><%= d[:type] %>&emsp;</div>
      <% end %>
      <div class="content"><%= d[:value] %></div>
      <% idx += 1 %>
    <% end %>
  </div>
<% end %>

<% unless @meta.rights.nil? || @meta.rights.empty?  %>
  <div class="block">
    <div class="block-label">License</div>
    <div class="content"><%= @meta.rights %></div>
  </div>
<% end %>

<% unless @meta.sizes.nil? || @meta.sizes.empty?  %>
  <div class="block">
    <div class="block-label">Size</div>
    <% idx = 0 %>
    <% for d in @meta.sizes %>
      <% if idx > 0  %>
        <div class="label">&emsp;</div>
      <% end %>
      <div class="content"><%= d%></div>
      <% idx += 1 %>
    <% end %>
  </div>
<% end %>

<% unless @meta.language.nil? || @meta.language.empty?  %>
  <div class="block">
    <div class="block-label">Language</div>
    <div class="content"><%= @meta.language %></div>
  </div>
<% end %>

<% unless @meta.dates.nil? || @meta.dates.empty?  %>
  <div class="block">
    <div class="block-label">Dates</div>
    <% for d in @meta.dates %>
      <div class="label"><%= d[:type] %>&emsp;</div>
      <div class="content"><%= d[:value] %></div>
    <% end %>
  </div>
<% end %>

<% unless @meta.version.nil? || @meta.version.empty?  %>
  <div class="block">
    <div class="block-label">Version</div>
    <div class="content"><%= @meta.version %></div>
  </div>
<% end %>

<% unless @meta.formats.nil? || @meta.formats.empty?  %>
  <div class="block">
    <div class="block-label">Formats</div>
    <% idx = 0 %>
    <% for d in @meta.formats %>
      <% if idx > 0  %>
        <div class="label">&emsp;</div>
      <% end %>
      <div class="content"><%= d%></div>
      <% idx += 1 %>
    <% end %>
  </div>
<% end %>

<% unless @meta.alternate_identifiers.nil? || @meta.alternate_identifiers.empty?  %>
  <div class="block">
    <div class="block-label">Alternate identifiers</div>
    <% for d in @meta.alternate_identifiers %>
      <div class="label"><%= d[:type] %>&emsp;</div>
      <div class="content"><%= d[:value] %></div>
    <% end %>
  </div>
<% end %>

<% unless @meta.related_identifiers.nil? || @meta.related_identifiers.empty?  %>
  <div class="block">
    <div class="block-label">Related identifiers</div>
    <% for d in @meta.related_identifiers %>
      <div class="label"><%= d[:relation_type] %>&emsp;</div>
      <div class="content"><%= d[:identifier_type].downcase %>:<%= d[:value] %></div>
    <% end %>
</div>
<% end %>

<% unless @meta.contributors.nil? || @meta.contributors.empty?  %>
  <div class="block">
    <div class="block-label">Contributors</div>
    <% for d in @meta.contributors %>
      <div class="label"><%= d[:type] %>&emsp;</div>
      <div class="content"><%= d[:value] %></div>
    <% end %>
  </div>
<% end %>

-->

<div class="block">
  <div class="block-label">Other formats</div>
  <#list allMedia as media>
    <div class="label">&emsp;</div>
    <div class="content"><a href="../${media}/${doi}">${media}</a></div>
  </#list>
</div>

</div>


</body>
</html>
