@misc{https://doi.org/${doi},
  doi = {${doi}},
  url = {https://doi.org/${doi}},
  author = {<#list creators! as c>${escapeLatex(c)}<#sep> and </#list>},
  publisher = {${escapeLatex(publisher)!}},
  title = {${escapeLatex(titles[0].value)}},
  year = {${publicationYear!}}
}
