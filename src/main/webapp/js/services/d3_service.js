app.service('d3Service', function() {

  return {
      getResponsiveCanvas: function(id, margin, height, width) {
        return d3.select('#' + id)
              .append('div')
              .classed('svg-container', true)
              .append('svg')
              .attr('preserveAspectRatio', 'xMinYMin meet')
              .attr('viewBox', '0 0 ' + (width + margin.left + margin.right) + ' ' + (height + margin.top + margin.bottom))
              .classed('svg-content-responsive', true)
              .append('g')
              .attr('transform', 'translate(' + margin.left + ', ' + margin.top + ')');
      }
  }

});