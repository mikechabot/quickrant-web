app.service('d3Service', function() {

  return {
      getResponsiveCanvas: function(id, margin, height, width) {
        return d3.select('#' + id)
              .append('div')
              .classed('svg-container', true)
              .append('svg')
              .attr('id', 'svg-' + id)
              .attr('preserveAspectRatio', 'xMinYMin meet')
              .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
              .classed('svg-content-responsive', true)
              .append('g')
              .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');
      },
      updateViewbox: function(id, margin, width, height) {
          return d3.select('#svg-' + id)
              .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
              //.attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');
      },
      addAxes: function(svg, height) {
          svg
              .append('g')
              .attr('class', 'x axis')
              .attr('transform', 'translate(0, ' + (height) + ')');

          svg
              .append('g')
              .attr('class', 'y axis')
              .attr('transform', 'translate(0,0)');
      }
  }

});